package ensime.consierge

import org.http4s.HttpService
import play.api.libs.json.Json

import scalaz.concurrent.Task
import org.http4s.client._
import org.http4s.dsl._

/**
 * Created by dani on 16/01/2016.
 */
class WebhookHandler extends Transport with Environment with MessageSubmission {

  val config = Configuration.load

  val service = HttpService {
    case req @ POST => {
      //    val tsk = newHandler(req.bodyAsText.toString)
      //    tsk.attemptRun match {
      //
      Ok("Foo")
    }
  }

  def postComment(issue: Issue): Unit = {

  }

}
