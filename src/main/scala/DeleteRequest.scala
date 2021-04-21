/**
 * Created by KaNguy - 04/14/2021
 * File DeleteRequest.scala
 */

// Data streaming and serialization
import java.io.{ BufferedReader, IOException, InputStream, InputStreamReader, Reader }

// Networking and web
import java.net.{ URL, HttpURLConnection }

// Scala IO Source
import scala.io.Source.{ fromURL, fromInputStream }

// Collections
import scala.collection.immutable.HashMap

// Local utilities
import util.{ Convert, HandleHeaders }

class DeleteRequest(var url: String) {
  private val requestMethod: String = "DELETE"
  private val convert: Convert = new Convert()
  private val handleHeaders: HandleHeaders = new HandleHeaders()

  def DELETE(url: String, headers: Array[Array[String]] = Array[Array[String]](), connectTimeout: Int = 5000, readTimeout: Int = 5000): String = {
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

// Testing here
object DeleteRequest {
  def main(args: Array[String]): Unit = {
    val deleteRequest: DeleteRequest = new DeleteRequest("https://reqbin.com/sample/delete/json")
    val data = deleteRequest.DELETE(deleteRequest.url, Array(Array("Accept", "*/*"), Array("User-Agent", "*")))
    println(data)
  }
}