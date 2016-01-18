package ensime.consierge

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, FlatSpec}
import org.scalatest.time.{Millis, Seconds, Span}
import play.api.libs.json.Json

import scala.util.{Success, Try}

/**
 * Created by dani on 16/01/2016.
 */
class WebhookSpec extends FlatSpec with Matchers with ScalaFutures {


  val mockBody =
    """
      |{
      |   "action":"opened",
      |   "issue":{
      |      "url":"https://api.github.com/repos/chauek/testing/issues/3",
      |      "labels_url":"https://api.github.com/repos/chauek/testing/issues/3/labels{/name}",
      |      "comments_url":"https://api.github.com/repos/chauek/testing/issues/3/comments",
      |      "events_url":"https://api.github.com/repos/chauek/testing/issues/3/events",
      |      "html_url":"https://github.com/chauek/testing/issues/3",
      |      "id":127023325,
      |      "number":3,
      |      "title":"Test issue 3",
      |      "user":{
      |         "login":"chauek",
      |         "id":248806,
      |         "avatar_url":"https://avatars.githubusercontent.com/u/248806?v=3",
      |         "gravatar_id":"",
      |         "url":"https://api.github.com/users/chauek",
      |         "html_url":"https://github.com/chauek",
      |         "followers_url":"https://api.github.com/users/chauek/followers",
      |         "following_url":"https://api.github.com/users/chauek/following{/other_user}",
      |         "gists_url":"https://api.github.com/users/chauek/gists{/gist_id}",
      |         "starred_url":"https://api.github.com/users/chauek/starred{/owner}{/repo}",
      |         "subscriptions_url":"https://api.github.com/users/chauek/subscriptions",
      |         "organizations_url":"https://api.github.com/users/chauek/orgs",
      |         "repos_url":"https://api.github.com/users/chauek/repos",
      |         "events_url":"https://api.github.com/users/chauek/events{/privacy}",
      |         "received_events_url":"https://api.github.com/users/chauek/received_events",
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
      |      "full_name":"chauek/testing",
      |      "owner":{
      |         "login":"chauek",
      |         "id":248806,
      |         "avatar_url":"https://avatars.githubusercontent.com/u/248806?v=3",
      |         "gravatar_id":"",
      |         "url":"https://api.github.com/users/chauek",
      |         "html_url":"https://github.com/chauek",
      |         "followers_url":"https://api.github.com/users/chauek/followers",
      |         "following_url":"https://api.github.com/users/chauek/following{/other_user}",
      |         "gists_url":"https://api.github.com/users/chauek/gists{/gist_id}",
      |         "starred_url":"https://api.github.com/users/chauek/starred{/owner}{/repo}",
      |         "subscriptions_url":"https://api.github.com/users/chauek/subscriptions",
      |         "organizations_url":"https://api.github.com/users/chauek/orgs",
      |         "repos_url":"https://api.github.com/users/chauek/repos",
      |         "events_url":"https://api.github.com/users/chauek/events{/privacy}",
      |         "received_events_url":"https://api.github.com/users/chauek/received_events",
      |         "type":"User",
      |         "site_admin":false
      |      },
      |      "private":false,
      |      "html_url":"https://github.com/chauek/testing",
      |      "description":"",
      |      "fork":false,
      |      "url":"https://api.github.com/repos/chauek/testing",
      |      "forks_url":"https://api.github.com/repos/chauek/testing/forks",
      |      "keys_url":"https://api.github.com/repos/chauek/testing/keys{/key_id}",
      |      "collaborators_url":"https://api.github.com/repos/chauek/testing/collaborators{/collaborator}",
      |      "teams_url":"https://api.github.com/repos/chauek/testing/teams",
      |      "hooks_url":"https://api.github.com/repos/chauek/testing/hooks",
      |      "issue_events_url":"https://api.github.com/repos/chauek/testing/issues/events{/number}",
      |      "events_url":"https://api.github.com/repos/chauek/testing/events",
      |      "assignees_url":"https://api.github.com/repos/chauek/testing/assignees{/user}",
      |      "branches_url":"https://api.github.com/repos/chauek/testing/branches{/branch}",
      |      "tags_url":"https://api.github.com/repos/chauek/testing/tags",
      |      "blobs_url":"https://api.github.com/repos/chauek/testing/git/blobs{/sha}",
      |      "git_tags_url":"https://api.github.com/repos/chauek/testing/git/tags{/sha}",
      |      "git_refs_url":"https://api.github.com/repos/chauek/testing/git/refs{/sha}",
      |      "trees_url":"https://api.github.com/repos/chauek/testing/git/trees{/sha}",
      |      "statuses_url":"https://api.github.com/repos/chauek/testing/statuses/{sha}",
      |      "languages_url":"https://api.github.com/repos/chauek/testing/languages",
      |      "stargazers_url":"https://api.github.com/repos/chauek/testing/stargazers",
      |      "contributors_url":"https://api.github.com/repos/chauek/testing/contributors",
      |      "subscribers_url":"https://api.github.com/repos/chauek/testing/subscribers",
      |      "subscription_url":"https://api.github.com/repos/chauek/testing/subscription",
      |      "commits_url":"https://api.github.com/repos/chauek/testing/commits{/sha}",
      |      "git_commits_url":"https://api.github.com/repos/chauek/testing/git/commits{/sha}",
      |      "comments_url":"https://api.github.com/repos/chauek/testing/comments{/number}",
      |      "issue_comment_url":"https://api.github.com/repos/chauek/testing/issues/comments{/number}",
      |      "contents_url":"https://api.github.com/repos/chauek/testing/contents/{+path}",
      |      "compare_url":"https://api.github.com/repos/chauek/testing/compare/{base}...{head}",
      |      "merges_url":"https://api.github.com/repos/chauek/testing/merges",
      |      "archive_url":"https://api.github.com/repos/chauek/testing/{archive_format}{/ref}",
      |      "downloads_url":"https://api.github.com/repos/chauek/testing/downloads",
      |      "issues_url":"https://api.github.com/repos/chauek/testing/issues{/number}",
      |      "pulls_url":"https://api.github.com/repos/chauek/testing/pulls{/number}",
      |      "milestones_url":"https://api.github.com/repos/chauek/testing/milestones{/number}",
      |      "notifications_url":"https://api.github.com/repos/chauek/testing/notifications{?since,all,participating}",
      |      "labels_url":"https://api.github.com/repos/chauek/testing/labels{/name}",
      |      "releases_url":"https://api.github.com/repos/chauek/testing/releases{/id}",
      |      "created_at":"2015-11-16T22:41:05Z",
      |      "updated_at":"2015-11-16T22:41:05Z",
      |      "pushed_at":"2015-11-16T22:41:05Z",
      |      "git_url":"git://github.com/chauek/testing.git",
      |      "ssh_url":"git@github.com:chauek/testing.git",
      |      "clone_url":"https://github.com/chauek/testing.git",
      |      "svn_url":"https://github.com/chauek/testing",
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
      |      "login":"chauek",
      |      "id":248806,
      |      "avatar_url":"https://avatars.githubusercontent.com/u/248806?v=3",
      |      "gravatar_id":"",
      |      "url":"https://api.github.com/users/chauek",
      |      "html_url":"https://github.com/chauek",
      |      "followers_url":"https://api.github.com/users/chauek/followers",
      |      "following_url":"https://api.github.com/users/chauek/following{/other_user}",
      |      "gists_url":"https://api.github.com/users/chauek/gists{/gist_id}",
      |      "starred_url":"https://api.github.com/users/chauek/starred{/owner}{/repo}",
      |      "subscriptions_url":"https://api.github.com/users/chauek/subscriptions",
      |      "organizations_url":"https://api.github.com/users/chauek/orgs",
      |      "repos_url":"https://api.github.com/users/chauek/repos",
      |      "events_url":"https://api.github.com/users/chauek/events{/privacy}",
      |      "received_events_url":"https://api.github.com/users/chauek/received_events",
      |      "type":"User",
      |      "site_admin":false
      |   }
      |}
    """.stripMargin

  "The 'issues' request body" should "parse correctly" in {
    val info = Json.parse(mockBody).as[IssueCreatedInfo]
    info.issue.id should be (127023325)
  }
}
