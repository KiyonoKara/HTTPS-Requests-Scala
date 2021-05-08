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

// Collections

// Local utilities
import util.{Constants, Convert, MutableHeadings}

class DeleteRequest(var url: String = null) {
  private val requestMethod: String = Constants.DELETE
  private val convert: Convert = new Convert()
  private val handleHeaders: MutableHeadings = new MutableHeadings()

  def DELETE(url: String = this.url, headers: Array[Array[String]] = Array[Array[String]](), connectTimeout: Int = 5000, readTimeout: Int = 5000): String = {
    // Constants
    val requestMethod: String = this.requestMethod
    val convert = this.convert
    val handleHeaders = this.handleHeaders

    // Establishes connection
    val connection = new URL(url).openConnection.asInstanceOf[HttpURLConnection]
    // Sets a timeout
    connection.setConnectTimeout(connectTimeout)
    // Sets a reading timeout
    connection.setReadTimeout(readTimeout)
    // Sets the request method to DELETE
    connection.setRequestMethod(requestMethod)

    // Adds headers if any are provided
    if (headers.nonEmpty) {
      val hashMapHeaders = convert.From2DtoHashMapMAX2(headers.asInstanceOf[Array[Array[Any]]])
      handleHeaders.addHeaders(connection, hashMapHeaders.asInstanceOf[collection.mutable.HashMap[String, String]])
    }

    // Basic input stream for data
    val inputStream = connection.getInputStream
    val content = fromInputStream(inputStream).mkString
    if (inputStream != null) inputStream.close()
    // Return the content or data
    content
  }
}