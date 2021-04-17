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

// Utils
import java.util.zip.GZIPInputStream

// Local utils
import util.{ Convert, HandleHeaders }

class FormURL(var url: String)

class GetRequest(var url: String) {
  def defaultGET(url: String): String = {
    val source = fromURL(url)
    val str = source.mkString
    source.close()
    str
  }

  def GET(url: String, headers: Array[Array[String]] = Array[Array[String]](), compressed: Boolean = true, connectTimeout: Int = 5000, readTimeout: Int = 5000): String = {
    // Constants
    val requestMethod: String = "GET"

    // Establishes connection
    val connection = new URL(url).openConnection.asInstanceOf[HttpURLConnection]
    // Sets a timeout
    connection.setConnectTimeout(connectTimeout)
    // Sets a reading timeout
    connection.setReadTimeout(readTimeout)
    // Sets the request method and defaults it to get if there is no other provided one
    connection.setRequestMethod(requestMethod)

    // Adds headers
    if (headers.nonEmpty) {
      val hashMapHeaders = new Convert().From2DtoHashMapMAX2(headers.asInstanceOf[Array[Array[Any]]])
      new HandleHeaders().addHeaders(connection, hashMapHeaders.asInstanceOf[collection.mutable.HashMap[String, String]])
    }

    // GZIP or not
    if (compressed) {
      // Set encoding to gzip
      connection.setRequestProperty("Accept-Encoding", "gzip")

      // GZIP
      var reader: Reader = null
      if (connection.getContentEncoding.equals("gzip")) {
        reader = new InputStreamReader(new GZIPInputStream(connection.getInputStream))

        // Empty char value
        var ch: Int = 0

        // String Builder to add to the final string
        val stringBuilder: StringBuilder = new StringBuilder()

        // Appending the data to a String Builder
        while (true) {
          ch = reader.read()
          if (ch == -1) {
            return stringBuilder.toString()
          }

          stringBuilder.append(ch.asInstanceOf[Char]).toString
        }
      }
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
object GetRequest {
  def main(args: Array[String]): Unit = {
    val getRequest: GetRequest = new GetRequest("https://docs.scala-lang.org")
    val data = getRequest.GET(getRequest.url, Array(Array("Accept", "*/*"), Array("User-Agent", "*")))
    println(data)
  }
}