package ensime.consierge

import org.scalatest.{ FlatSpec, Matchers }

import scala.concurrent.duration._

/**
 * Created by owner1 on 13/02/16.
 */
class CommentsSpec extends FlatSpec with Comments with Matchers with ConfigMock with HttpMock {

  "The comments uri" should "build properly" in {
    commentsUri(1).toString() should be("https://api.github.com/repos/owner123/repo123/issues/1/comments")
  }

  "The comments list" should "parse properly" in {
    val resp = issueCommentsTask(2).runFor(10.seconds)
    resp should be(List(IssueComment(12345, User(1, "message-user")), IssueComment(1234, User(1, "message-user"))))
  }

  it should "parse properly empty list" in {
    val resp = issueCommentsTask(1).runFor(10.seconds)
    resp should be(List())
  }
}
