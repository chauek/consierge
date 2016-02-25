package ensime.consierge

import org.scalatest.{ Matchers, FlatSpec }

/**
 * Created by chauek on 13/02/16.
 */
class MessageSubmissionSpec extends FlatSpec with MessageSubmission with Matchers with ConfigMock with HttpMock with WebhookMock {

  behavior of "The message"

  it should "be posted" in {
    shouldMessageBePosted(newUserBodyMock) should be(true)
  }

  it should "not be posted on repo owned by someone else" in {
    shouldMessageBePosted(differentOwnerBodyMock) should be(false)
  }

  it should "not be posted on different repo of the same owner" in {
    shouldMessageBePosted(differentRepoBodyMock) should be(false)
  }

  it should "not be posted on different action" in {
    shouldMessageBePosted(closedActionBodyMock) should be(false)
  }

  it should "not be posted when was posted before" in {
    shouldMessageBePosted(alreadyCommentedBodyMock) should be(false)
  }

  it should "not be posted when created by one of contributors" in {
    shouldMessageBePosted(contributorBodyMock) should be(false)
  }

}
