package ensime.consierge

import java.net.BindException

import com.typesafe.scalalogging.StrictLogging

object MainApp extends App with StrictLogging {

  try {
    val whService = new WebhookHandler
    val whServer = new WebhookServer
    whServer.run(whService.service)
  } catch {
    case ex: BindException =>
      logger.error(ex.getMessage)
  }

}
