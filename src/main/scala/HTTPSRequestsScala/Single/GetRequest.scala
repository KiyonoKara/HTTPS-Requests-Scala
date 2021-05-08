package HTTPSRequestsScala.Single

/**
 * Created by KaNguy - 04/09/2021
 * File core.GetRequest.scala
 */

// Data streaming and serialization
import java.io.{InputStreamReader, Reader}

// Networking and web
import java.net.{HttpURLConnection, URL}

// Scala IO Source
import scala.io.Source.{fromInputStream, fromURL}

// Utils
import java.util.zip.GZIPInputStream

// Local utilities
import util.{Constants, Convert, MutableHeadings}

class GetRequest(var url: String = null) {
  // Private variables
  private val requestMethod: String = Constants.GET
  private val convert: Convert = new Convert()
  private val handleHeaders: MutableHeadings = new MutableHeadings()

  def defaultGET(url: String = this.url): String = {
    val source = fromURL(url)
    val str = source.mkString
    source.close()
    str
  }

  def GET(url: String = this.url, headers: Array[Array[String]] = Array[Array[String]](), compressed: Boolean = true, connectTimeout: Int = 5000, readTimeout: Int = 5000): String = {
    // Constants
    val requestMethod: String = this.requestMethod
    val convert: Convert = this.convert
    val handleHeaders: MutableHeadings = this.handleHeaders

    // Establishes connection
    val connection = new URL(url).openConnection.asInstanceOf[HttpURLConnection]
    // Sets a timeout
    connection.setConnectTimeout(connectTimeout)
    // Sets a reading timeout
    connection.setReadTimeout(readTimeout)
    // Sets the request method and defaults it to get if there is no other provided one
    connection.setRequestMethod(requestMethod)

    // Adds headers
    val hashMapHeaders = convert.From2DtoHashMapMAX2(headers.asInstanceOf[Array[Array[Any]]])
    if (headers.nonEmpty) {
      handleHeaders.addHeaders(connection, hashMapHeaders.asInstanceOf[collection.mutable.HashMap[String, String]])
    }

    val lowercaseHeaders = handleHeaders.lowerCaseHeaders(hashMapHeaders.asInstanceOf[collection.mutable.HashMap[String, String]])

    // GZIP or not
    if (compressed || lowercaseHeaders.getOrElse("accept-encoding", "none").equals("gzip")) {
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
      } else {
        this.defaultGET(url)
      }
    }

    // Basic input stream for data
    val inputStream = connection.getInputStream
    val content = fromInputStream(inputStream).mkString
    if (inputStream != null) inputStream.close()
    // Return the content or data
    content
  }

  def equals(str1: String, str2: String): Boolean = {
    if (str1 == str2) return true
    if ((str1 == null) || (str2 == null)) return false
    str1.equals(str2)
  }
}