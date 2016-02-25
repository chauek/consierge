package ensime.consierge

import com.typesafe.scalalogging.StrictLogging
import org.joda.time.DateTime
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.util.Try

sealed trait IssueState
object IssueState {
  case object Open extends IssueState
  case object Closed extends IssueState

  implicit val reads: Reads[IssueState] = new Reads[IssueState] {
    def reads(json: JsValue) = json match {
      case JsString("open") => JsSuccess(Open)
      case JsString("closed") => JsSuccess(Closed)
      case other => JsError("bad.issue.state: " + other)
    }
  }
}

case class User(id: Int, login: String)

object User {
  implicit val reads: Reads[User] =
    ((__ \ "id").read[Int] and
      (__ \ "login").read[String])(User.apply _)
}

case class Issue(id: Int, number: Int, user: User) //, title: String, body: String, state: IssueState, createdAt: DateTime, updatedAt: DateTime)

object Issue {
  implicit val reads: Reads[Issue] = (
    (__ \ "id").read[Int] ~
    (__ \ "number").read[Int] ~
    (__ \ "user").read[User] // ~
  //    (__ \ "title").read[String] ~
  //    (__ \ "body").read[String] ~
  //    (__ \ "state").read[IssueState] ~
  //    (__ \ "created_at").read[String].map(DateTime.parse) ~
  //    (__ \ "updated_at").read[String].map(DateTime.parse)
  )(Issue.apply _)
}

case class Repository(id: Int, full_name: String)

object Repository {
  implicit val reads: Reads[Repository] = (
    (__ \ "id").read[Int] ~
    (__ \ "full_name").read[String]
  )(Repository.apply _)
}

case class IssueComment(id: Int, user: User)

object IssueComment {
  implicit val reads: Reads[IssueComment] = (
    (__ \ "id").read[Int] ~
    (__ \ "user").read[User]
  )(IssueComment.apply _)
}

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

case class IssueCreatedInfo(action: String, issue: Issue, repository: Repository)

object IssueCreatedInfo {
  implicit val reads: Reads[IssueCreatedInfo] = (
    (__ \ "action").read[String] ~
    (__ \ "issue").read[Issue] ~
    (__ \ "repository").read[Repository]
  )(IssueCreatedInfo.apply _)
}