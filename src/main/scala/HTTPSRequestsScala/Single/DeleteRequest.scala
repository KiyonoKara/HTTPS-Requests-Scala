package HTTPSRequestsScala.Single

/**
 * Created by KaNguy - 04/14/2021
 * File core.DeleteRequest.scala
 */

// Data streaming and serialization

// Networking and web
import java.net.{HttpURLConnection, URL}

// Scala IO Source
import scala.io.Source.fromInputStream

// Local utilities
import HTTPSRequestsScala.util.{Constants, HandleHeaders}

class DeleteRequest(var url: String = null) {
  private val requestMethod: String = Constants.DELETE

  def DELETE(url: String = this.url, headers: Iterable[(String, String)] = Nil, connectTimeout: Int = 5000, readTimeout: Int = 5000): String = {
    // Constants
    val requestMethod: String = this.requestMethod

    // Establishes connection
    val connection = new URL(url).openConnection.asInstanceOf[HttpURLConnection]
    // Sets a timeout
    connection.setConnectTimeout(connectTimeout)
    // Sets a reading timeout
    connection.setReadTimeout(readTimeout)
    // Sets the request method to DELETE
    connection.setRequestMethod(requestMethod)

    // Adds headers
    HandleHeaders.setHeaders(connection, headers)

    // Basic input stream for data
    val inputStream = connection.getInputStream
    val content = fromInputStream(inputStream).mkString
    if (inputStream != null) inputStream.close()
    // Return the content or data
    content
  }
}