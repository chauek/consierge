package ensime.consierge

import org.http4s.{ EntityDecoder, Uri }
import org.http4s.client.Client
import scalaz.concurrent.Task
import org.scalamock.scalatest.MockFactory
import org.scalatest.{ Matchers, FlatSpec }

import scala.concurrent.duration._

/**
 * Created by chauek on 13/02/16.
 */
class ContributorSpec extends FlatSpec with Contributor with Matchers with MockFactory {

  def config = Configuration(
    owner = "owner123",
    repo = "repo123",
    message = "message.txt",
    credentials = Credentials(
      username = "username123",
      accessToken = "12345"
    ),
    pollInterval = new FiniteDuration(1, MINUTES),
    timeout = new FiniteDuration(1, MINUTES)
  )

  val mockContributorsBody =
    """
      |[
      |  {
      |    "login": "user",
      |    "id": 1,
      |    "avatar_url": "https://avatars.githubusercontent.com/u/1?v=3",
      |    "gravatar_id": "",
      |    "url": "https://api.github.com/users/user",
      |    "html_url": "https://github.com/user",
      |    "followers_url": "https://api.github.com/users/user/followers",
      |    "following_url": "https://api.github.com/users/user/following{/other_user}",
      |    "gists_url": "https://api.github.com/users/user/gists{/gist_id}",
      |    "starred_url": "https://api.github.com/users/user/starred{/owner}{/repo}",
      |    "subscriptions_url": "https://api.github.com/users/user/subscriptions",
      |    "organizations_url": "https://api.github.com/users/user/orgs",
      |    "repos_url": "https://api.github.com/users/user/repos",
      |    "events_url": "https://api.github.com/users/user/events{/privacy}",
      |    "received_events_url": "https://api.github.com/users/user/received_events",
      |    "type": "User",
      |    "site_admin": false,
      |    "contributions": 1
      |  },
      |  {
      |    "login": "user2",
      |    "id": 2,
      |    "avatar_url": "https://avatars.githubusercontent.com/u/2?v=3",
      |    "gravatar_id": "",
      |    "url": "https://api.github.com/users/user2",
      |    "html_url": "https://github.com/user2",
      |    "followers_url": "https://api.github.com/users/user2/followers",
      |    "following_url": "https://api.github.com/users/user2/following{/other_user}",
      |    "gists_url": "https://api.github.com/users/user2/gists{/gist_id}",
      |    "starred_url": "https://api.github.com/users/user2/starred{/owner}{/repo}",
      |    "subscriptions_url": "https://api.github.com/users/user2/subscriptions",
      |    "organizations_url": "https://api.github.com/users/user2/orgs",
      |    "repos_url": "https://api.github.com/users/user2/repos",
      |    "events_url": "https://api.github.com/users/user2/events{/privacy}",
      |    "received_events_url": "https://api.github.com/users/user2/received_events",
      |    "type": "User",
      |    "site_admin": false,
      |    "contributions": 1
      |  }
      |]
    """.stripMargin

  override def getHttpResponse(uri: Uri): Task[String] = Task[String] { mockContributorsBody }

  "The contributor uri" should "build properly" in {
    contributorsUri.toString() should be("https://api.github.com/repos/owner123/repo123/contributors")
  }

  "The contributors list " should "parse properly" in {
    val resp = contributorsTask.runFor(10.seconds)
    resp should be(List(User(1, "user"), User(2, "user2")))
  }

}
