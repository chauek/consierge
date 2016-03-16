package ensime.consierge

import scala.concurrent.duration._
import org.http4s.server.Server
import org.http4s.HttpService
import org.http4s.server.blaze.BlazeBuilder

/**
 * Created by chauek on 12/03/16.
 */
class WebhookServer {

  val config = Configuration.load

  def run(whService: HttpService) = {

    BlazeBuilder.bindHttp(config.server.port)
      //.withWebSockets(true)
      .mountService(whService, config.server.root)
      .run
      .awaitShutdown()
  }

}
