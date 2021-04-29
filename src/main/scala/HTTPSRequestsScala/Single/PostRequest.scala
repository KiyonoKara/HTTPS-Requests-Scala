package HTTPSRequestsScala.Single

/**
 * Created by KaNguy - 04/13/2021
 * File core.PostRequest.scala
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
import util.{Constants, Convert, HandleHeaders}

class PostRequest(var url: String = null, var data: String = "{}") {
  private val requestMethod: String = Constants.POST
  private val convert: Convert = new Convert()
  private val handleHeaders: HandleHeaders = new HandleHeaders()

  def POST(url: String = this.url, data: String = this.data, headers: Array[Array[String]] = Array[Array[String]]()): Any = {
    // Open and establish the URL connection
    val connection: HttpURLConnection = new URL(url).openConnection.asInstanceOf[HttpURLConnection]

    // Initialize handlers
    val handleHeaders: HandleHeaders = this.handleHeaders

    // Set it to POST
    val requestMethod: String = this.requestMethod
    connection.setRequestMethod(requestMethod)

    // Adds headers
    val hashMapHeaders = convert.From2DtoHashMapMAX2(headers.asInstanceOf[Array[Array[Any]]])
    if (headers.nonEmpty) {
      handleHeaders.addHeaders(connection, hashMapHeaders.asInstanceOf[collection.mutable.HashMap[String, String]])
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
