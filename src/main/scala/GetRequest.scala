/**
 * Created by KaNguy - 04/09/2021
 * File GetRequest.scala
 */

// Data streaming and serialization
import java.io.{ BufferedReader, IOException, InputStream, InputStreamReader, Reader }

// Networking and web
import java.net.{ URL, HttpURLConnection }

// Scala IO Source
import scala.io.Source.{ fromURL, fromInputStream }

class FormURL(var url: String)

class GetRequest(var url: String) {
  def defaultGET(url: String): String = {
    val source = fromURL(url)
    val str = source.mkString
    source.close()
    str
  }

  def GET(url: String, connectTimeout: Int = 5000, readTimeout: Int = 5000, requestMethod: String = "GET"): String = {
    // Establishes connection
    val connection = new URL(url).openConnection.asInstanceOf[HttpURLConnection]
    // Sets a timeout
    connection.setConnectTimeout(connectTimeout)
    // Sets a reading timeout
    connection.setReadTimeout(readTimeout)
    // Sets the request method and defaults it to get if there is no other provided one
    connection.setRequestMethod(requestMethod)

    // Input stream for data
    val inputStream = connection.getInputStream
    val content = fromInputStream(inputStream).mkString
    if (inputStream != null) inputStream.close()
    // Return the content or data
    content
  }
}

object GetRequest {
  def main(args: Array[String]): Unit = {
    val getRequest: GetRequest = new GetRequest("https://docs.scala-lang.org")
    val data = getRequest.GET(getRequest.url)
    println(data)
  }
}