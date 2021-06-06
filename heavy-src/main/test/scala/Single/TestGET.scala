package Single

import HTTPSRequestsScala.Single.GetRequest

object TestGET {
  def main(args: Array[String]): Unit = {
    // With default compression
    val getRequest: GetRequest = new GetRequest("https://docs.scala-lang.org")
    val data = getRequest.GET(getRequest.url, Map("Accept" -> "*/*", "User-Agent" -> "*"))
    println(data)

    // With compression disabled by the parameters, but enabled through headers
    val getRequest2: GetRequest = new GetRequest("https://docs.scala-lang.org")
    val data2 = getRequest2.GET(getRequest2.url, Map("Accept" -> "*/*", "User-Agent" -> "*", "Accept-Encoding" -> "gzip"))
    println(data2)
  }
}