package ensime.consierge

import org.http4s.Uri
import org.http4s.dsl._
import play.api.libs.json.Json

import scalaz.concurrent.Task

/**
 * Created by chauek on 13/02/16.
 */
trait Comments extends Transport with Environment with HttpClient {

  def commentsUri(issueNumber: Int) =
    Uri.fromString(Protocol + Host + s"/repos/${config.owner}/${config.repo}/issues/${issueNumber}/comments").getOrElse(uri(""))

  def issueCommentsTask(issueNumber: Int): Task[Seq[IssueComment]] = getHttpResponse(commentsUri(issueNumber))
    .map(Json.parse(_).as[Seq[IssueComment]])
}
