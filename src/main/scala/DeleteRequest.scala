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

class FormURL(var url: String)

class DeleteRequest(var url: String) {
  private val requestMethod: String = "DELETE"

  def DELETE(url: String, headers: HashMap[String, String] = HashMap.empty[String, String], connectTimeout: Int = 5000, readTimeout: Int = 5000): String = {
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

    if (headers.nonEmpty) {
      headers.foreach(hash => {
        val k: String = hash._1
        val v: String = hash._2
        connection.addRequestProperty(k, v)
      })
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
    val data = deleteRequest.DELETE(deleteRequest.url, HashMap("Accept" -> "*/*"))
    println(data)
  }
}