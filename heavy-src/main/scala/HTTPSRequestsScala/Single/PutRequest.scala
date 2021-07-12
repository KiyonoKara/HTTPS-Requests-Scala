package HTTPSRequestsScala.Single

/**
 * Created by KaNguy - 04/17/2021
 * File core.PutRequest.scala
 */

// Java IO streaming imports
import java.io.{InputStream, OutputStream}

// URL connection for requesting
import java.net.{HttpURLConnection, URL}

// Charset(s)
import java.nio.charset.StandardCharsets

// Scala IO Source
import scala.io.Source.fromInputStream

// Utilities
import HTTPSRequestsScala.utility.{Constants, HandleHeaders}

class PutRequest(var url: String = null, var data: String = "{}") {
  private val requestMethod: String = Constants.PUT

  def PUT(url: String = this.url, data: String = this.data, headers: Iterable[(String, String)] = Nil): Any = {
    // Open and establish the URL connection
    val connection: HttpURLConnection = new URL(url).openConnection.asInstanceOf[HttpURLConnection]

    // Set it to PUT
    val requestMethod: String = this.requestMethod
    connection.setRequestMethod(requestMethod)

    // Sets headers
    if (headers.nonEmpty) {
      HandleHeaders.setHeaders(connection, headers)
    }

    // Output to true
    connection.setDoOutput(true)

    // Processing the data
    val byte: Array[Byte] = data.getBytes(StandardCharsets.UTF_8)
    val length: Int = byte.length
    connection.setFixedLengthStreamingMode(length)

    try {
      // Write to the request
      val outputStream: OutputStream = connection.getOutputStream
      outputStream.write(byte, 0, byte.length)
    } finally {
      // Get output of request
      val inputStream: InputStream = connection.getInputStream
      val content: String = fromInputStream(inputStream).mkString
      inputStream.close()
      return content
    }
  }
}
