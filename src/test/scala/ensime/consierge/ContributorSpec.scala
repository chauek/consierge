package ensime.consierge

import org.scalatest.{ Matchers, FlatSpec }

import scala.concurrent.duration._

/**
 * Created by chauek on 13/02/16.
 */
class ContributorSpec extends FlatSpec with Contributor with Matchers with ConfigMock with HttpMock {

  "The contributor uri" should "build properly" in {
    contributorsUri.toString() should be("https://api.github.com/repos/owner123/repo123/contributors")
  }

  "The contributors list " should "parse properly" in {
    val resp = contributorsTask.runFor(10.seconds)
    resp should be(List(User(1, "owner123"), User(2, "message-user"), User(3, "contributor")))
  }

}
