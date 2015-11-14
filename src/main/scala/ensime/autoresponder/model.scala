package ensime.autoresponder

import akka.actor.ActorSystem
import akka.actor.Cancellable
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ ContentTypes, HttpEntity, HttpMethods, HttpRequest, HttpResponse, Uri }
import akka.stream.Materializer
import akka.stream.scaladsl.{ Flow, Sink, Source }
import akka.util.ByteString
import com.typesafe.scalalogging.StrictLogging
import org.joda.time.DateTime
import play.api.libs.json.{ JsValue, Json }
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Try

case class Configuration(
  owner: String,
  repo: String,
  message: String,
  accessToken: String,
  pollInterval: FiniteDuration,
  timeout: FiniteDuration)

sealed trait IssueState
object IssueState {
  case object Open extends IssueState
  case object Closed extends IssueState
}

case class User(id: Int)

case class Issue(id: Int, number: Int, user: User, title: String, body: String, state: IssueState, createdAt: DateTime, updatedAt: DateTime)

case class CommentResponse(id: Int, url: String, createdAt: DateTime)

object CommentResponse {
  import play.api.libs.json._
  import play.api.libs.functional.syntax._

  implicit val reads: Reads[CommentResponse] = (
    (JsPath \ "id").read[Int] and
    (JsPath \ "url").read[String] and
    (JsPath \ "created_at").read[String].map(DateTime.parse)
  )(CommentResponse.apply _)
}

trait Environment {
  def config: Configuration
}

trait Flows extends CommentSubmission with IssueFetching with StrictLogging {
  def searchFlow(implicit mat: Materializer, as: ActorSystem): Flow[Issue, Issue, Unit]

  def responseSink: Sink[CommentResponse, Future[Unit]] = Sink.foreach[CommentResponse](resp =>
    logger.info(s"Submitted a new comment: $resp")
  )

  def graph(implicit mat: Materializer, as: ActorSystem, ec: ExecutionContext) = issueSource
    .via(searchFlow)
    .via(respondFlow)
    .to(responseSink)
}

trait Transport {
  val Host = "api.github.com"

  def pool[T](implicit mat: Materializer, as: ActorSystem): Flow[(HttpRequest, T), (Try[HttpResponse], T), Unit] = Http().superPool[T]()
}
