package util

/**
 * Created by KaNguy - 04/26/2021
 * File util.OutputReader.scala
 */

// IO imports
import java.io.{InputStreamReader, InputStream, Reader}

// Networking
import java.net.HttpURLConnection

// Compression
import java.util.zip.GZIPInputStream

object OutputReader {
  def read(connection: HttpURLConnection, inputStream: InputStream = null): String = {
    var connectionInputStream: InputStream = null
    if (inputStream != null) connectionInputStream = inputStream else connectionInputStream = connection.getInputStream

    var reader: Reader = null
    if (connection.getContentEncoding != null) {
      reader = new InputStreamReader(new GZIPInputStream(connectionInputStream))
    } else {
      reader = new InputStreamReader(connection.getInputStream)
    }

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