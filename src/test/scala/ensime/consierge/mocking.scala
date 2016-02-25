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
      username = "message-user",
      accessToken = "12345"
    ),
    pollInterval = new FiniteDuration(1, MINUTES),
    timeout = new FiniteDuration(1, MINUTES)
  )

}

trait HttpMock extends HttpClient {

  override def getHttpResponse(uri: Uri): Task[String] = uri.toString match {
    case "https://api.github.com/repos/owner123/repo123/contributors" => Task[String] { mockContributorsBody }
    case "https://api.github.com/repos/owner123/repo123/issues/1/comments" => Task[String] { mockCommentsBodyEmpty }
    case "https://api.github.com/repos/owner123/repo123/issues/2/comments" => Task[String] { mockCommentsBodyAlreadyCommented }
    case unknown => throw new Exception("HttpMock: Unknown URI: " + unknown)
  }

  val mockContributorsBody =
    """
      |[
      |  {
      |    "login": "owner123",
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
      |    "login": "message-user",
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
      |  },
      |  {
      |    "login": "contributor",
      |    "id": 3,
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

  val mockCommentsBodyEmpty =
    """
      |[
      |]
    """.stripMargin

  val mockCommentsBodyAlreadyCommented =
    """
      |[
      |  {
      |    "url": "https://api.github.com/repos/owner1/repo12/issues/comments/12345",
      |    "html_url": "https://github.com/owner1/repo12/issues/2#issuecomment-12345",
      |    "issue_url": "https://api.github.com/repos/owner1/repo12/issues/2",
      |    "id": 12345,
      |    "user": {
      |      "login": "message-user",
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
      |      "login": "message-user",
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

trait WebhookMock {

  /**
   * Mock body for positive test
   * action = opened
   * issue.user.id = 4
   * issue.number = 1
   * repository.full_name = owner123/repo123
   */
  val newUserBodyMock =
    """
      |{
      |   "action":"opened",
      |   "issue":{
      |      "url":"https://api.github.com/repos/octocat/testing/issues/1",
      |      "labels_url":"https://api.github.com/repos/octocat/testing/issues/1/labels{/name}",
      |      "comments_url":"https://api.github.com/repos/octocat/testing/issues/1/comments",
      |      "events_url":"https://api.github.com/repos/octocat/testing/issues/1/events",
      |      "html_url":"https://github.com/octocat/testing/issues/1",
      |      "id":127023325,
      |      "number":1,
      |      "title":"Test issue",
      |      "user":{
      |         "login":"newuser",
      |         "id":4,
      |         "avatar_url":"https://avatars.githubusercontent.com/u/1?v=3",
      |         "gravatar_id":"",
      |         "url":"https://api.github.com/users/octocat",
      |         "html_url":"https://github.com/octocat",
      |         "followers_url":"https://api.github.com/users/octocat/followers",
      |         "following_url":"https://api.github.com/users/octocat/following{/other_user}",
      |         "gists_url":"https://api.github.com/users/octocat/gists{/gist_id}",
      |         "starred_url":"https://api.github.com/users/octocat/starred{/owner}{/repo}",
      |         "subscriptions_url":"https://api.github.com/users/octocat/subscriptions",
      |         "organizations_url":"https://api.github.com/users/octocat/orgs",
      |         "repos_url":"https://api.github.com/users/octocat/repos",
      |         "events_url":"https://api.github.com/users/octocat/events{/privacy}",
      |         "received_events_url":"https://api.github.com/users/octocat/received_events",
      |         "type":"User",
      |         "site_admin":false
      |      },
      |      "labels":[
      |
      |      ],
      |      "state":"open",
      |      "locked":false,
      |      "assignee":null,
      |      "milestone":null,
      |      "comments":0,
      |      "created_at":"2016-01-16T12:58:04Z",
      |      "updated_at":"2016-01-16T12:58:04Z",
      |      "closed_at":null,
      |      "body":"Third test issue"
      |   },
      |   "repository":{
      |      "id":46307741,
      |      "name":"testing",
      |      "full_name":"owner123/repo123",
      |      "owner":{
      |         "login":"owner123",
      |         "id":1,
      |         "avatar_url":"https://avatars.githubusercontent.com/u/1?v=3",
      |         "gravatar_id":"",
      |         "url":"https://api.github.com/users/octocat",
      |         "html_url":"https://github.com/octocat",
      |         "followers_url":"https://api.github.com/users/octocat/followers",
      |         "following_url":"https://api.github.com/users/octocat/following{/other_user}",
      |         "gists_url":"https://api.github.com/users/octocat/gists{/gist_id}",
      |         "starred_url":"https://api.github.com/users/octocat/starred{/owner}{/repo}",
      |         "subscriptions_url":"https://api.github.com/users/octocat/subscriptions",
      |         "organizations_url":"https://api.github.com/users/octocat/orgs",
      |         "repos_url":"https://api.github.com/users/octocat/repos",
      |         "events_url":"https://api.github.com/users/octocat/events{/privacy}",
      |         "received_events_url":"https://api.github.com/users/octocat/received_events",
      |         "type":"User",
      |         "site_admin":false
      |      },
      |      "private":false,
      |      "html_url":"https://github.com/octocat/testing",
      |      "description":"",
      |      "fork":false,
      |      "url":"https://api.github.com/repos/octocat/testing",
      |      "forks_url":"https://api.github.com/repos/octocat/testing/forks",
      |      "keys_url":"https://api.github.com/repos/octocat/testing/keys{/key_id}",
      |      "collaborators_url":"https://api.github.com/repos/octocat/testing/collaborators{/collaborator}",
      |      "teams_url":"https://api.github.com/repos/octocat/testing/teams",
      |      "hooks_url":"https://api.github.com/repos/octocat/testing/hooks",
      |      "issue_events_url":"https://api.github.com/repos/octocat/testing/issues/events{/number}",
      |      "events_url":"https://api.github.com/repos/octocat/testing/events",
      |      "assignees_url":"https://api.github.com/repos/octocat/testing/assignees{/user}",
      |      "branches_url":"https://api.github.com/repos/octocat/testing/branches{/branch}",
      |      "tags_url":"https://api.github.com/repos/octocat/testing/tags",
      |      "blobs_url":"https://api.github.com/repos/octocat/testing/git/blobs{/sha}",
      |      "git_tags_url":"https://api.github.com/repos/octocat/testing/git/tags{/sha}",
      |      "git_refs_url":"https://api.github.com/repos/octocat/testing/git/refs{/sha}",
      |      "trees_url":"https://api.github.com/repos/octocat/testing/git/trees{/sha}",
      |      "statuses_url":"https://api.github.com/repos/octocat/testing/statuses/{sha}",
      |      "languages_url":"https://api.github.com/repos/octocat/testing/languages",
      |      "stargazers_url":"https://api.github.com/repos/octocat/testing/stargazers",
      |      "contributors_url":"https://api.github.com/repos/octocat/testing/contributors",
      |      "subscribers_url":"https://api.github.com/repos/octocat/testing/subscribers",
      |      "subscription_url":"https://api.github.com/repos/octocat/testing/subscription",
      |      "commits_url":"https://api.github.com/repos/octocat/testing/commits{/sha}",
      |      "git_commits_url":"https://api.github.com/repos/octocat/testing/git/commits{/sha}",
      |      "comments_url":"https://api.github.com/repos/octocat/testing/comments{/number}",
      |      "issue_comment_url":"https://api.github.com/repos/octocat/testing/issues/comments{/number}",
      |      "contents_url":"https://api.github.com/repos/octocat/testing/contents/{+path}",
      |      "compare_url":"https://api.github.com/repos/octocat/testing/compare/{base}...{head}",
      |      "merges_url":"https://api.github.com/repos/octocat/testing/merges",
      |      "archive_url":"https://api.github.com/repos/octocat/testing/{archive_format}{/ref}",
      |      "downloads_url":"https://api.github.com/repos/octocat/testing/downloads",
      |      "issues_url":"https://api.github.com/repos/octocat/testing/issues{/number}",
      |      "pulls_url":"https://api.github.com/repos/octocat/testing/pulls{/number}",
      |      "milestones_url":"https://api.github.com/repos/octocat/testing/milestones{/number}",
      |      "notifications_url":"https://api.github.com/repos/octocat/testing/notifications{?since,all,participating}",
      |      "labels_url":"https://api.github.com/repos/octocat/testing/labels{/name}",
      |      "releases_url":"https://api.github.com/repos/octocat/testing/releases{/id}",
      |      "created_at":"2015-11-16T22:41:05Z",
      |      "updated_at":"2015-11-16T22:41:05Z",
      |      "pushed_at":"2015-11-16T22:41:05Z",
      |      "git_url":"git://github.com/octocat/testing.git",
      |      "ssh_url":"git@github.com:octocat/testing.git",
      |      "clone_url":"https://github.com/octocat/testing.git",
      |      "svn_url":"https://github.com/octocat/testing",
      |      "homepage":null,
      |      "size":0,
      |      "stargazers_count":0,
      |      "watchers_count":0,
      |      "language":null,
      |      "has_issues":true,
      |      "has_downloads":true,
      |      "has_wiki":true,
      |      "has_pages":false,
      |      "forks_count":0,
      |      "mirror_url":null,
      |      "open_issues_count":3,
      |      "forks":0,
      |      "open_issues":3,
      |      "watchers":0,
      |      "default_branch":"master"
      |   },
      |   "sender":{
      |      "login":"octocat",
      |      "id":1,
      |      "avatar_url":"https://avatars.githubusercontent.com/u/1?v=3",
      |      "gravatar_id":"",
      |      "url":"https://api.github.com/users/octocat",
      |      "html_url":"https://github.com/octocat",
      |      "followers_url":"https://api.github.com/users/octocat/followers",
      |      "following_url":"https://api.github.com/users/octocat/following{/other_user}",
      |      "gists_url":"https://api.github.com/users/octocat/gists{/gist_id}",
      |      "starred_url":"https://api.github.com/users/octocat/starred{/owner}{/repo}",
      |      "subscriptions_url":"https://api.github.com/users/octocat/subscriptions",
      |      "organizations_url":"https://api.github.com/users/octocat/orgs",
      |      "repos_url":"https://api.github.com/users/octocat/repos",
      |      "events_url":"https://api.github.com/users/octocat/events{/privacy}",
      |      "received_events_url":"https://api.github.com/users/octocat/received_events",
      |      "type":"User",
      |      "site_admin":false
      |   }
      |}
    """.stripMargin

  /**
   * Mock body for different owner
   * action = opened
   * issue.user.id = 4
   * issue.number = 1
   * repository.full_name = owner/repo123
   */
  val differentOwnerBodyMock =
    """
      |{
      |   "action":"opened",
      |   "issue":{
      |      "id":127023325,
      |      "number":1,
      |      "user":{
      |         "login":"newuser",
      |         "id":4
      |      }
      |   },
      |   "repository":{
      |      "id":46307741,
      |      "full_name":"owner/testing"
      |   }
      |}
    """.stripMargin

  /**
   * Mock body for different repo
   * action = opened
   * issue.user.id = 4
   * issue.number = 1
   * repository.full_name = owner123/repo111
   */
  val differentRepoBodyMock =
    """
      |{
      |   "action":"opened",
      |   "issue":{
      |      "id":127023325,
      |      "number":1,
      |      "user":{
      |         "login":"newuser",
      |         "id":4
      |      }
      |   },
      |   "repository":{
      |      "id":46307741,
      |      "full_name":"owner123/repo111"
      |   }
      |}
    """.stripMargin

  /**
   * Mock body for closed issue
   * action = closed
   * issue.user.id = 4
   * issue.number = 1
   * repository.full_name = owner123/repo123
   */
  val closedActionBodyMock =
    """
      |{
      |   "action":"closed",
      |   "issue":{
      |      "id":127023325,
      |      "number":1,
      |      "user":{
      |         "login":"newuser",
      |         "id":4
      |      }
      |   },
      |   "repository":{
      |      "id":46307741,
      |      "full_name":"owner123/repo123"
      |   }
      |}
    """.stripMargin

  /**
   * Mock body for issue having the message already
   * action = closed
   * issue.user.id = 4
   * issue.number = 2
   * repository.full_name = owner123/repo123
   */
  val alreadyCommentedBodyMock =
    """
      |{
      |   "action":"opened",
      |   "issue":{
      |      "id":127023325,
      |      "number":2,
      |      "user":{
      |         "login":"newuser",
      |         "id":4
      |      }
      |   },
      |   "repository":{
      |      "id":46307741,
      |      "full_name":"owner123/repo123"
      |   }
      |}
    """.stripMargin

  /**
   * Mock body for issue created by contributor
   * action = closed
   * issue.user.id = 2
   * issue.number = 1
   * repository.full_name = owner123/repo123
   */
  val contributorBodyMock =
    """
      |{
      |   "action":"opened",
      |   "issue":{
      |      "id":127023325,
      |      "number":1,
      |      "user":{
      |         "login":"contributor",
      |         "id":2
      |      }
      |   },
      |   "repository":{
      |      "id":46307741,
      |      "full_name":"owner123/repo123"
      |   }
      |}
    """.stripMargin

}