/**
 * Created by KaNguy - 04/13/2021
 * File PostRequest.scala
 */

// Java IO streaming imports
import java.io.{OutputStream, InputStream}

// URL connection for requesting
import java.net.{HttpURLConnection, URL}

// Charset(s)
import java.nio.charset.StandardCharsets

// Scala IO Source
import scala.io.Source.fromInputStream

class PostRequest(var url: String, var data: String) {
  def POST(url: String = this.url, data: String = this.data): Any = {
    // Open and establish the URL connection
    val connection: HttpURLConnection = new URL(url).openConnection.asInstanceOf[HttpURLConnection]

    // Set it to POST
    connection.setRequestMethod("POST")

    // Setting the headers and other important data
    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
    connection.setRequestProperty("User-Agent", "HTTPS-Request-Scala")
    connection.setRequestProperty("Accept", "application/json")
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

// Testing here
object PostRequest {
  def main(args: Array[String]): Unit = {
    val postRequest: PostRequest = new PostRequest("https://reqres.in/api/users", "{\"name\": \"Li Xi\", \"job\": \"Scala POST\"}")
    val post = postRequest.POST()
    println(post)
  }
}
