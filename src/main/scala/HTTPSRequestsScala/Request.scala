package HTTPSRequestsScala

/**
 * Created by KaNguy - 04/26/2021
 * File HTTPSRequestsScala.Request.scala
 */

// Networking and web
import java.net.{HttpURLConnection, URL}

// Scala IO Source
import scala.io.Source.fromInputStream


// Request classes
import RequestTypes.WritableRequests

// Local utilities
import util.{Constants, Convert, HandleHeaders, OutputReader}

/** Main class for making HTTP/HTTPS requests
 *
 * @param url - String; Provide an URL with its path (if you are requesting with the path)
 * @param method - String; Request method, refer to the Constants file for supported methods
 * @param headers - 2D Array; Provide headers in the form of a 2D array where the first element contains the key, second element contains the value
 */
class Request(var url: String = null, var method: String = "GET", headers: Array[Array[String]] = Array[Array[String]]()) {
  // Constants and handles
  private val convert: Convert = new Convert()
  private val handleHeaders: HandleHeaders = new HandleHeaders()
  private val writableRequests: WritableRequests = new WritableRequests()

  /** Class method that ultimately does the requesting
   *
   * @param url - String; Provide an URL
   * @param method - String; Request method, defaults to the class' default method
   * @param headers - 2D Array; Headers for requesting
   * @param data - String; Preferably JSON data that is in the form of a string
   * @return
   */
  def request(url: String = this.url, method: String = this.method, headers: Array[Array[String]] = this.headers, data: String = null): String = {
    // Create the connection from the provided URL
    val connection = new URL(url).openConnection.asInstanceOf[HttpURLConnection]

    // Set the request method
    connection.setRequestMethod(method)

    // Timeouts
    connection.setConnectTimeout(Constants.DEFAULT_TIMEOUT)
    connection.setReadTimeout(Constants.DEFAULT_TIMEOUT)

    // Adds headers
    val hashMapHeaders = this.convert.From2DtoHashMapMAX2(headers.asInstanceOf[Array[Array[Any]]])
    if (headers.nonEmpty) {
      this.handleHeaders.addHeaders(connection, hashMapHeaders.asInstanceOf[collection.mutable.HashMap[String, String]])
    }

    if (method.toUpperCase.equals(Constants.GET)) {
      return OutputReader.read(connection, connection.getInputStream)
    }

    if (method.toUpperCase.equals(Constants.POST) || method.toUpperCase.equals(Constants.DELETE) || method.toUpperCase.equals(Constants.PUT) || method.toUpperCase.equals(Constants.PATCH)) {
      return writableRequests.request(url, method, data, headers)
    }

    // Input stream for data with a GET request if all of the requests fail
    val inputStream = connection.getInputStream
    val content = fromInputStream(inputStream).mkString
    if (inputStream != null) inputStream.close()
    // Return the content or data, read-only
    content
  }
}