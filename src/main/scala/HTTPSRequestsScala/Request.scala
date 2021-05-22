package HTTPSRequestsScala

/**
 * Created by KaNguy - 04/26/2021
 * File HTTPSRequestsScala.Request.scala
 */

// Networking and web
import java.net.{HttpURLConnection, URL, ConnectException}
import javax.net.ssl.SSLException

// Scala IO Source
import scala.io.Source.fromInputStream

// Request classes
import RequestTypes.WritableRequests

// Local utilities
import HTTPSRequestsScala.util.{Constants, OutputReader, HandleHeaders, Utility}

// Other
import java.lang.reflect.Field

/** Main class for making HTTP/HTTPS requests
 *
 * @param url - String; Provide an URL with its path (if you are requesting with the path)
 * @param method - String; Request method, refer to the Constants file for supported methods
 * @param headers - Iterable[(String, String)]; Headers in the form of a Map collection is primarily valid
 */
class Request(var url: String = null, var method: String = Constants.GET, headers: Iterable[(String, String)] = Nil) {
  private val writableRequests: WritableRequests = new WritableRequests()
  private lazy val methodField: Field = {
    val method = classOf[HttpURLConnection].getDeclaredField("method")
    method.setAccessible(true)
    method
  }


  /** Class method that ultimately does the requesting
   *
   * @param url - String; Provide an URL
   * @param method - String; Request method, defaults to the class' default method
   * @param headers - Iterable[(String, String)]; Headers for requesting
   * @param data - String; Preferably JSON data that is in the form of a string
   * @param parameters - Iterable[(String, String)]; URL parameters that can be used for querying
   * @return {String}
   */
  def request(url: String = this.url, method: String = this.method, headers: Iterable[(String, String)] = this.headers, data: String = null, parameters: Iterable[(String, String)] = Nil): String = {
    // Parse the URL along with the parameters
    val requestURL: String = Utility.createURL(url, parameters)
    val parsedURL: URL = new URL(requestURL)

    // Create the connection from the provided URL
    var connection: HttpURLConnection = null
    try {
      connection = parsedURL.openConnection.asInstanceOf[HttpURLConnection] match {
        case _: HttpURLConnection => parsedURL.openConnection.asInstanceOf[HttpURLConnection];
      }
    } catch {
      case connectException: ConnectException =>
        connectException.printStackTrace()
        throw connectException
      case sslException: SSLException =>
        sslException.printStackTrace()
        throw sslException
    }

    // Set the request method
    if (Constants.HTTPMethods.contains(method.toUpperCase)) {
      connection.setRequestMethod(method.toUpperCase)
    } else {
      /** For PATCH requests, the method will default to POST.
       * PATCH requests can still be done with X-HTTP-Method-Override header that changes the request method.
       *
       * Example for adding the PATCH override:
       * {{{
       *   val PATCH: String = new Request().request("http://localhost:8080/echo",
       *                                         "PATCH", Map("Accept" -> "*",
       *                                         "User-Agent" -> "*",
       *                                         "X-HTTP-Method-Override" -> "PATCH"),
       *                                         data = "{\"message\": \"PATCH message\"}")
       * }}}
       *
       */
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

    // Timeouts
    connection.setConnectTimeout(Constants.DEFAULT_TIMEOUT)
    connection.setReadTimeout(Constants.DEFAULT_TIMEOUT)

    // Sets headers
    if (headers.nonEmpty) {
      HandleHeaders.setHeaders(connection, headers)
    }

    if (method.toUpperCase.equals(Constants.GET)) {
      return OutputReader.read(connection, connection.getInputStream)
    }

    if (method.toUpperCase.equals(Constants.POST) || method.toUpperCase.equals(Constants.DELETE) || method.toUpperCase.equals(Constants.PUT) || method.toUpperCase.equals(Constants.PATCH)) {
      return writableRequests.request(requestURL, method, data, headers)
    }

    // Input stream for data with a GET request if all of the requests fail
    val inputStream = connection.getInputStream
    val content = fromInputStream(inputStream).mkString
    if (inputStream != null) inputStream.close()
    // Return the content or data, read-only
    content
  }

  /** Can turn collections into JSON data as a string
   *
   * @param collections - Any; Accepts collections and primitive types
   * @return {String}
   */
  def collectionToJSON(collections: Any): String = {
    Utility.CollectionsToJSON(collections).toString
  }

  /** Method to turn maps that have only two strings per index into JSON data as a string
   *
   * @param map - Map[String, String]; String map with only two strings, this is for regular JSON objects that have no nesting or lists
   * @return {String}
   */
  def mapToJSON(map: Map[String, String] = Map.empty): String = {
    Utility.singleMapToJSON(map).toString
  }
}