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
import HTTPSRequestsScala.util.{Constants, OutputReader, HandleHeaders}

// Other
import java.lang.reflect.Field

class WritableRequests() {
  private lazy val methodField: Field = {
    val method = classOf[HttpURLConnection].getDeclaredField("method")
    method.setAccessible(true)
    method
  }

  /**
   *
   * @param url Provide an url for the request
   * @param method Provide a method, there are no defaults since all are formatted the same but have vastly different outcomes.
   * @param data Usually data is provided as JSON in the form of a string, but any data in the form of a string is accepted.
   * @param headers Iterable[(String, String)]; Headers in the form of a Map collection is primarily valid
   * @return output Most writable requests have output, thus, it will always guarantee an output as a string
   */
  def request(url: String, method: String, data: String = null, headers: Iterable[(String, String)] = Nil): String = {
    // Meant for POST, PUT, PATCH, and DELETE requests
    val connection: HttpURLConnection = new URL(url).openConnection.asInstanceOf[HttpURLConnection]

    // Set the request method
    if (Constants.HTTPMethods.contains(method.toUpperCase)) {
      connection.setRequestMethod(method.toUpperCase)
    } else {
      connection match {
        case httpURLConnection: HttpURLConnection =>
          httpURLConnection.getClass.getDeclaredFields.find(_.getName == "delegate") foreach { i =>
            i.setAccessible(true)
            this.methodField.set(i.get(httpURLConnection), method.toUpperCase)
          }
        case other =>
          this.methodField.set(other, method.toUpperCase)
      }
    }

    if (headers.nonEmpty) {
      HandleHeaders.setHeaders(connection, headers)
    }
    try {
      this.writeToRequest(connection, method, data)
    } catch {
      case _: Exception => OutputReader.read(connection, connection.getInputStream)
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
    if (theMethod.equals(Constants.POST) || theMethod.equals(Constants.PUT) || theMethod.equals(Constants.PATCH)) connection.setDoOutput(true)

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
