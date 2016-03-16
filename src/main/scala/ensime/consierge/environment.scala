package ensime.consierge

import com.typesafe.scalalogging.StrictLogging
import scala.concurrent.duration._
import scala.util.Try

case class ServerConf(
  port: Int,
  root: String,
  githubWebhooksPath: String)

case class Credentials(
  username: String,
  accessToken: String)

case class Configuration(
  server: ServerConf,
  owner: String,
  repo: String,
  message: String,
  credentials: Credentials,
  pollInterval: FiniteDuration,
  timeout: FiniteDuration)

object Configuration {
  def load: Configuration = {
    val config = com.typesafe.config.ConfigFactory.load()

    def int(key: String): Int =
      Option(config.getInt(key))
        .getOrElse(sys.error(s"Config key not found: $key"))

    def string(key: String): String =
      Option(config.getString(key))
        .getOrElse(sys.error(s"Config key not found: $key"))

    def duration(key: String): FiniteDuration =
      Option(config.getDuration(key))
        .map(_.toMillis.milliseconds)
        .getOrElse(sys.error(s"Config key not found: $key"))

    def file(key: String): String =
      scala.io.Source.fromFile(string(key)).mkString

    Configuration(
      server = ServerConf(
        port = int("consierge.server.port"),
        root = string("consierge.server.root"),
        githubWebhooksPath = string("consierge.server.githubWebhooksPath")
      ),
      owner = string("consierge.owner"),
      repo = string("consierge.repo"),
      message = file("consierge.messageFile"),
      credentials = Credentials(
        username = string("consierge.credentials.username"),
        accessToken = string("consierge.credentials.accessToken")
      ),
      pollInterval = duration("consierge.pollInterval"),
      timeout = duration("consierge.timeout")
    )
  }
}

trait Environment {
  def config: Configuration
}

trait Transport extends StrictLogging {
  this: Environment =>

  val Protocol = "https://"
  val Host = "api.github.com"
  val DownloadTimeout = 10.second

  //  private lazy val authHeaders: Iterable[HttpHeader] =
  //    List(Authorization(GenericHttpCredentials("token", config.credentials.accessToken)))

}
