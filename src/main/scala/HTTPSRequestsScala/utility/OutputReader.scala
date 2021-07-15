package HTTPSRequestsScala.utility

/**
 * Created by KaNguy - 04/26/2021
 * File HTTPSRequestsScala.utility.OutputReader.scala
 */

// IO imports
import java.io.{InputStream, InputStreamReader, Reader}

// Networking
import java.net.HttpURLConnection

// Compression
import java.util.zip.{GZIPInputStream, DeflaterInputStream}

object OutputReader {
  /** Reads output of a connection established via the HttpURLConnection class
   *
   * @param connection HttpURLConnection
   * @param inputStream InputStream
   * @return String of the output
   */
  def read(connection: HttpURLConnection, inputStream: InputStream = null): String = {
    var connectionInputStream: InputStream = null
    if (inputStream != null) connectionInputStream = inputStream else connectionInputStream = connection.getInputStream

    // Set the reader to a null value before reading the output
    var reader: Reader = null

    // GZIP & Deflate data streaming
    if (connection.getContentEncoding != null && connection.getContentEncoding.nonEmpty) {
      connection.getContentEncoding match {
        case "gzip" => reader = new InputStreamReader(new GZIPInputStream(connectionInputStream))
        case "deflate" => reader = new InputStreamReader(new DeflaterInputStream(connectionInputStream))
        case _ => reader = new InputStreamReader(connection.getInputStream)
      }
    } else reader = new InputStreamReader(connection.getInputStream)


    // Empty char value
    var ch: Int = 0

    // String Builder to add to the final string
    val stringBuilder: StringBuilder = new StringBuilder()

    // Appending the data to a String Builder
    while (ch != -1) {
      ch = reader.read()
      if (ch == -1) {
        return stringBuilder.toString()
      }

      stringBuilder.append(ch.asInstanceOf[Char]).toString
    }
    stringBuilder.toString
  }
}