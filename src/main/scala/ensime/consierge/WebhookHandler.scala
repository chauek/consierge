package ensime.consierge

import org.http4s.{ HttpService, Uri }
import play.api.libs.json.Json

import scalaz.{ -\/, \/- }
import scalaz.concurrent.Task
import org.http4s.client._
import org.http4s.dsl._

/**
 * Created by dani on 16/01/2016.
 */
class WebhookHandler extends Transport with Environment with Contributor with Comments {

  val client = org.http4s.client.blaze.defaultClient

  val config = Configuration.load

  val service = HttpService {
    case req @ POST => {
      //    val tsk = newHandler(req.bodyAsText.toString)
      //    tsk.attemptRun match {
      //
      Ok("Foo")
    }
  }

  def postIssueCommentUri(issueNumber: Int) =
    Uri.fromString(Protocol + Host + s"/repos/${config.owner}/${config.repo}/issues/${issueNumber}/comments").getOrElse(uri(""))

  def postCommentTask(issueNumber: Int): Task[CommentResponse] = {
    val request = POST(postIssueCommentUri(issueNumber), Json.stringify(Json.obj("body" -> config.message)))
    client.fetchAs[String](request)
      .map(Json.parse(_).as[CommentResponse])
  }

  def shouldCommentBePosted(body: String): Boolean = {

    val issueCreatedInfo = Json.parse(body).as[IssueCreatedInfo]

    val issue = issueCreatedInfo.issue

    // conditions
    val isIssueOpened = issueCreatedInfo.action == "opened"
    val isNewUserTask = contributorsTask.map(!_.exists(_.id == issue.user.id))
    val alreadyPostedTask = issueCommentsTask(issue.number).map(_.exists(_.user.login == config.credentials.username))

    val shouldIPost: Task[Boolean] = for {
      isUserAContributor <- isNewUserTask
      isCommentAlreadyPosted <- alreadyPostedTask
      isIssueInfoOpened = isIssueOpened
    } yield (!isUserAContributor && !isCommentAlreadyPosted && isIssueInfoOpened)

    shouldIPost.attemptRunFor(DownloadTimeout) match {
      case -\/(throwable) => {
        logger.debug("Problem determining whether to post a comment: " + throwable)
        false
      }
      case \/-(result) => result
    }
  }

  def postComment(issue: Issue): Unit = {

  }

}
