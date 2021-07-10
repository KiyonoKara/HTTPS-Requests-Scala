package HTTPSRequestsScala.util

/**
 * Created by KaNguy - 04/26/2021
 * File HTTPSRequestsScala.util.OutputReader.scala
 */

// IO imports
import java.io.{InputStream, InputStreamReader, Reader}

// Networking
import java.net.HttpURLConnection

// Compression
import java.util.zip.GZIPInputStream
import java.util.zip.DeflaterInputStream

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

    // GZIP data streaming
    if (connection.getContentEncoding != null && connection.getContentEncoding.equals("gzip")) {
      reader = new InputStreamReader(new GZIPInputStream(connectionInputStream))
    } else reader = new InputStreamReader(connection.getInputStream)

    // Deflate data streaming
    if (connection.getContentEncoding != null && connection.getContentEncoding.equals("deflate")) {
      reader = new InputStreamReader(new DeflaterInputStream(connectionInputStream))
    } else reader = new InputStreamReader(connection.getInputStream)


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
    stringBuilder.toString
  }
}