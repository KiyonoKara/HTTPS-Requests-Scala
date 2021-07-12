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
import HTTPSRequestsScala.utility.{Constants, HandleHeaders, Utility}

class GetRequest(var url: String = null) {
  // Private variables
  private val requestMethod: String = Constants.GET

  def defaultGET(url: String = this.url, parameters: Iterable[(String, String)] = Nil): String = {
    val source = fromURL(Utility.createURL(url, parameters))
    val str = source.mkString
    source.close()
    str
  }

  def GET(url: String = this.url, headers: Iterable[(String, String)] = Nil, parameters: Iterable[(String, String)] = Nil, compressed: Boolean = true, connectTimeout: Int = 5000, readTimeout: Int = 5000): String = {
    // Constants
    val requestMethod: String = this.requestMethod

    val requestURL = Utility.createURL(url, parameters)

    // Establishes connection
    val connection = new URL(requestURL).openConnection.asInstanceOf[HttpURLConnection]
    // Sets a timeout
    connection.setConnectTimeout(connectTimeout)
    // Sets a reading timeout
    connection.setReadTimeout(readTimeout)
    // Sets the request method and defaults it to get if there is no other provided one
    connection.setRequestMethod(requestMethod)

    // Sets headers
    if (headers.nonEmpty) {
      HandleHeaders.setHeaders(connection, headers)
    }

    // GZIP or not
    val acceptEncodingKey: String = headers match {
      case map: Map[String, String] =>
        Utility.getKeyByValue(map, "gzip")
    }

    val acceptEncodingValue: String = headers match {
      case map: Map[String, String] =>
        map.getOrElse(acceptEncodingKey, "")
    }

    if (compressed || acceptEncodingValue.equals("gzip")) {
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