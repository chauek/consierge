package ensime.consierge

import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ Matchers, FlatSpec }
import org.scalatest.time.{ Millis, Seconds, Span }
import play.api.libs.json.Json

import scala.util.{ Success, Try }

/**
 * Created by dani on 16/01/2016.
 */
class WebhookSpec extends FlatSpec with Matchers with ScalaFutures with MockFactory {

  val mockBody =
    """
      |{
      |   "action":"opened",
      |   "issue":{
      |      "url":"https://api.github.com/repos/octocat/testing/issues/3",
      |      "labels_url":"https://api.github.com/repos/octocat/testing/issues/3/labels{/name}",
      |      "comments_url":"https://api.github.com/repos/octocat/testing/issues/3/comments",
      |      "events_url":"https://api.github.com/repos/octocat/testing/issues/3/events",
      |      "html_url":"https://github.com/octocat/testing/issues/3",
      |      "id":127023325,
      |      "number":3,
      |      "title":"Test issue 3",
      |      "user":{
      |         "login":"octocat",
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
      |      "full_name":"octocat/testing",
      |      "owner":{
      |         "login":"octocat",
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

  "The 'issues' request body" should "parse correctly" in {
    val info = Json.parse(mockBody).as[IssueCreatedInfo]
    info.issue.id should be(127023325)
  }
}
