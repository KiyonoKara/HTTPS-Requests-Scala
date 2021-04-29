package HTTPSRequestsScala.RequestTypes

/**
 * Created by KaNguy - 04/26/2021
 * File HTTPSRequestsScala.RequestTypes.WritableRequests.scala
 */

// Data streaming and serialization
import java.io.{InputStreamReader, Reader, InputStream, OutputStream}

// Networking and web
import java.net.{HttpURLConnection, URL}

// Charset(s)
import java.nio.charset.StandardCharsets

// Scala IO Source
import scala.io.Source.fromInputStream

// Utils
import java.util.zip.GZIPInputStream

// Local utils
import util.{Constants, Convert, HandleHeaders, OutputReader}

class WritableRequests() {
  def request(url: String, method: String, data: String = null, headers: Array[Array[String]] = Array[Array[String]]()): String = {
    val connection: HttpURLConnection = new URL(url).openConnection.asInstanceOf[HttpURLConnection]
    connection.setRequestMethod(method)
    val convert: Convert = new Convert()
    val handleHeaders: HandleHeaders = new HandleHeaders()
    val hashMapHeaders = convert.From2DtoHashMapMAX2(headers.asInstanceOf[Array[Array[Any]]])
    if (headers.nonEmpty) {
      handleHeaders.addHeaders(connection, hashMapHeaders.asInstanceOf[collection.mutable.HashMap[String, String]])
    }
    try {
      this.writeToRequest(connection, method, data)
    } catch {
      case _: Exception => OutputReader.read(connection, connection.getInputStream)
    }
  }

  def writeToRequest(connection: HttpURLConnection, method: String, data: String): String = {
    val theMethod: String = method.toUpperCase
    if (theMethod.equals(Constants.POST)) connection.setDoOutput(true)

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

// Temporary tests, will move to new file later
object Test {
  def main(args: Array[String]): Unit = {
    val writableRequests: WritableRequests = new WritableRequests()
    val post = writableRequests.request(
      "https://reqres.in/api/users",
      "POST",
      "{\"name\": \"Li Xi\", \"job\": \"Scala POST\"}",
      Array(Array("Content-Type", "application/json; charset=UTF-8"), Array("User-Agent", "Scala"), Array("Accept", "application/json"))
    )
    println(post)
  }
}
