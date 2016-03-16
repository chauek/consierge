package ensime.consierge

import com.fasterxml.jackson.core.JsonParseException
import com.typesafe.scalalogging.StrictLogging

import org.http4s.HttpService
import org.http4s.dsl._

import scalaz.concurrent.Task
import scalaz.stream.Process

/**
 * Created by dani on 16/01/2016.
 */
class WebhookHandler extends Transport with Environment with StrictLogging with MessageSubmission {

  val config = Configuration.load

  val service = HttpService {

    case req @ POST -> Root / config.server.githubWebhooksPath =>
      println("-----------------")
      try {
        val body: Process[Task, String] = req.bodyAsText
        val bodyString: String = body.runLastOr("").run
        logger.info(s"Got body: $bodyString")
        println("shouldMessageBePosted: " + shouldMessageBePosted(bodyString))
        println("-----------------")
        Ok("Hello github.")
      } catch {
        case e: JsonParseException =>
          logger.error(e.getMessage)
          println("-----------------")
          BadRequest("Could not parse the content")

        case e: Throwable =>
          val er = e
          logger.error(e.getMessage)
          println("-----------------")
          BadRequest("")
      }

    case GET -> Root / "ping" =>
      Ok("pong")
  }

}
