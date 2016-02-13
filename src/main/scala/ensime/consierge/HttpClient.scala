package ensime.consierge

import org.http4s.Uri

import scalaz.concurrent.Task

/**
 * Created by chauek on 13/02/16.
 */
trait HttpClient {

  val httpClient = org.http4s.client.blaze.defaultClient

  def getHttpResponse(uri: Uri): Task[String] = httpClient.getAs[String](uri)

}
