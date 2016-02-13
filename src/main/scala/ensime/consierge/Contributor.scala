package ensime.consierge

import org.http4s.Uri
import org.http4s.dsl._
import play.api.libs.json.Json

import scalaz.concurrent.Task

/**
 * Created by chauek on 13/02/16.
 */
trait Contributor extends Transport with Environment with HttpClient {

  def contributorsUri: Uri =
    Uri.fromString(Protocol + Host + s"/repos/${config.owner}/${config.repo}/contributors").getOrElse(uri(""))

  def contributorsTask: Task[Seq[User]] = getHttpResponse(contributorsUri)
    .map(Json.parse(_).as[Seq[User]])
}
