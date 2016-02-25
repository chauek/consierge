package ensime.consierge

import play.api.libs.json.Json

import org.http4s.client._
import org.http4s.dsl._
import scalaz.{ \/-, -\/ }
import scalaz.concurrent.Task

/**
 * Created by chauek on 13/02/16.
 */
trait MessageSubmission extends Contributor with Comments with HttpClient {

  def shouldMessageBePosted(body: String): Boolean = {

    val issueCreatedInfo = Json.parse(body).as[IssueCreatedInfo]

    val issue = issueCreatedInfo.issue

    // conditions
    val isIssueOpened = issueCreatedInfo.action == "opened"
    val isProperRepo = issueCreatedInfo.repository.full_name == s"${config.owner}/${config.repo}"
    val isNewUserTask = contributorsTask.map(!_.exists(_.id == issue.user.id))
    val alreadyPostedTask = issueCommentsTask(issue.number).map(_.exists(_.user.login == config.credentials.username))

    val shouldIPost: Task[Boolean] = for {
      isUserNotAContributor <- isNewUserTask
      isCommentAlreadyPosted <- alreadyPostedTask
      isIssueInfoOpened = isIssueOpened
      isProperRepoInfo = isProperRepo
    } yield (isUserNotAContributor && !isCommentAlreadyPosted && isIssueInfoOpened && isProperRepoInfo)

    shouldIPost.attemptRunFor(DownloadTimeout) match {
      case -\/(throwable) => {
        logger.debug(s"Problem determining whether to post a comment: $throwable")
        false
      }
      case \/-(result) => result
    }
  }

  val client = org.http4s.client.blaze.defaultClient

  def postCommentTask(issueNumber: Int): Task[CommentResponse] = {
    val request = POST(commentsUri(issueNumber), Json.stringify(Json.obj("body" -> config.message)))
    client.fetchAs[String](request)
      .map(Json.parse(_).as[CommentResponse])
  }

}
