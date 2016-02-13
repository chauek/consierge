package ensime.consierge

import org.http4s.Uri

import scala.concurrent.duration._
import scalaz.concurrent.Task

/**
 * Created by chauek on 13/02/16.
 */
trait ConfigMock {

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

}

trait HttpMock extends HttpClient {

  override def getHttpResponse(uri: Uri): Task[String] = uri.toString match {
    case "https://api.github.com/repos/owner123/repo123/contributors" => Task[String] { mockContributorsBody }
    case "https://api.github.com/repos/owner123/repo123/issues/1/comments" => Task[String] { mockCommentsBody1 }
  }

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

  val mockCommentsBody1 =
    """
      |[
      |  {
      |    "url": "https://api.github.com/repos/owner1/repo12/issues/comments/12345",
      |    "html_url": "https://github.com/owner1/repo12/issues/2#issuecomment-12345",
      |    "issue_url": "https://api.github.com/repos/owner1/repo12/issues/2",
      |    "id": 12345,
      |    "user": {
      |      "login": "owner1",
      |      "id": 1,
      |      "avatar_url": "https://avatars.githubusercontent.com/u/1?v=3",
      |      "gravatar_id": "",
      |      "url": "https://api.github.com/users/owner1",
      |      "html_url": "https://github.com/owner1",
      |      "followers_url": "https://api.github.com/users/owner1/followers",
      |      "following_url": "https://api.github.com/users/owner1/following{/other_user}",
      |      "gists_url": "https://api.github.com/users/owner1/gists{/gist_id}",
      |      "starred_url": "https://api.github.com/users/owner1/starred{/owner}{/repo}",
      |      "subscriptions_url": "https://api.github.com/users/owner1/subscriptions",
      |      "organizations_url": "https://api.github.com/users/owner1/orgs",
      |      "repos_url": "https://api.github.com/users/owner1/repos",
      |      "events_url": "https://api.github.com/users/owner1/events{/privacy}",
      |      "received_events_url": "https://api.github.com/users/owner1/received_events",
      |      "type": "User",
      |      "site_admin": false
      |    },
      |    "created_at": "2016-01-16T12:52:56Z",
      |    "updated_at": "2016-01-16T12:52:56Z",
      |    "body": "Next comment"
      |  },
      |  {
      |    "url": "https://api.github.com/repos/owner1/repo12/issues/comments/1234",
      |    "html_url": "https://github.com/owner1/repo12/issues/2#issuecomment-1234",
      |    "issue_url": "https://api.github.com/repos/owner1/repo12/issues/2",
      |    "id": 1234,
      |    "user": {
      |      "login": "owner1",
      |      "id": 1,
      |      "avatar_url": "https://avatars.githubusercontent.com/u/1?v=3",
      |      "gravatar_id": "",
      |      "url": "https://api.github.com/users/owner1",
      |      "html_url": "https://github.com/owner1",
      |      "followers_url": "https://api.github.com/users/owner1/followers",
      |      "following_url": "https://api.github.com/users/owner1/following{/other_user}",
      |      "gists_url": "https://api.github.com/users/owner1/gists{/gist_id}",
      |      "starred_url": "https://api.github.com/users/owner1/starred{/owner}{/repo}",
      |      "subscriptions_url": "https://api.github.com/users/owner1/subscriptions",
      |      "organizations_url": "https://api.github.com/users/owner1/orgs",
      |      "repos_url": "https://api.github.com/users/owner1/repos",
      |      "events_url": "https://api.github.com/users/owner1/events{/privacy}",
      |      "received_events_url": "https://api.github.com/users/owner1/received_events",
      |      "type": "User",
      |      "site_admin": false
      |    },
      |    "created_at": "2016-01-16T12:54:27Z",
      |    "updated_at": "2016-01-16T12:54:27Z",
      |    "body": "Third comment"
      |  }
      |]
    """.stripMargin

}