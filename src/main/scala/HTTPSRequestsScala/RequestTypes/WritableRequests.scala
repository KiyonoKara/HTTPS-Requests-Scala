package HTTPSRequestsScala.RequestTypes

/**
 * Created by KaNguy - 04/26/2021
 * File HTTPSRequestsScala.RequestTypes.WritableRequests.scala
 */

// Data streaming and serialization
import java.io.{InputStream, OutputStream}

// Networking and web
import java.net.{HttpURLConnection, URL}

// Charset(s)
import java.nio.charset.StandardCharsets

// Scala IO Source
import scala.io.Source.fromInputStream

// Local utils
import util.{Constants, OutputReader, HandleHeaders}

class WritableRequests() {
  /**
   *
   * @param url - String; Provide an url for the request
   * @param method - String; Provide a method, there are no defaults since all are formatted the same but have vastly different outcomes.
   * @param data - String; Usually data is provided as JSON in the form of a string, but any data in the form of a string is accepted.
   * @param headers - 2D Array; A 2D array which is `Array[Array[String]]`
   * @return output - String; Most writable requests have output, thus, it will always guarantee a string
   */
  def request(url: String, method: String, data: String = null, headers: Iterable[(String, String)] = Nil): String = {
    // Meant for POST, PUT, PATCH, and DELETE requests
    val connection: HttpURLConnection = new URL(url).openConnection.asInstanceOf[HttpURLConnection]
    connection.setRequestMethod(method)

    if (headers.nonEmpty) {
      HandleHeaders.setHeaders(connection, headers)
    }
    try {
      this.writeToRequest(connection, method, data)
    } catch {
      case _: Exception => OutputReader.read(connection, connection.getInputStream)
        // TODO: Add SSL handling for requests that throw the unsupported or unrecognized SSL message error
    }
  }

  /**
   *
   * @param connection - HttpURLConnection; The connection established will be used so it can be written to.
   * @param method - String; Method is always required, cannot default to a common request method
   * @param data - String; Preferably JSON data in the form of a string.
   * @return output - String; Generally returns the output of the Output Reader
   */
  def writeToRequest(connection: HttpURLConnection, method: String, data: String): String = {
    val theMethod: String = method.toUpperCase
    if (theMethod.equals(Constants.POST) || theMethod.equals(Constants.PUT)) connection.setDoOutput(true)

    // Processing the data
    val byte: Array[Byte] = data.getBytes(StandardCharsets.UTF_8)
    val length: Int = byte.length
    connection.setFixedLengthStreamingMode(length)

    try {
      // Write to the request
      val outputStream: OutputStream = connection.getOutputStream
      outputStream.write(byte, 0, byte.length)
      if (theMethod.equals(Constants.POST)) {
        outputStream.flush()
        outputStream.close()
      }
      // Get output of request
      val inputStream: InputStream = connection.getInputStream
      if (connection.getContentEncoding != null && connection.getContentEncoding.toLowerCase.equals("gzip")) {
        val content: String = OutputReader.read(connection, inputStream)
        content
      } else {
        val content: String = fromInputStream(inputStream).mkString
        inputStream.close()
        content
      }
    }
  }
}
