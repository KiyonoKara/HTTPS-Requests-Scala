/**
 * Created by KaNguy - 04/14/2021
 * File HandleHeaders.scala
 */

// Util package for this library
package util

// Networking and web
import java.net.HttpURLConnection

// Collection(s)
import scala.collection.mutable

class HandleHeaders(var headers: mutable.HashMap[String, String] = mutable.HashMap.empty[String, String]) {
  /** Sanitizes the headers by setting them all to lowercase, this is not recommended for authorization tokens and may invalidate it
   *
   * @param headers - HashMap of headers with the key and value
   * @return
   */
  def sanitizeHeaders(headers: mutable.HashMap[String, String] = this.headers): mutable.HashMap[String, String] = {
    if (headers.nonEmpty) {
      headers.foreach(hash => {
        val k: String = hash._1; val v: String = hash._2
        headers.remove(k)
        headers.put(k.toLowerCase, v.toLowerCase)
      })

      headers
    } else {
      headers
    }
  }

  def addHeaders(connection: HttpURLConnection, headers: mutable.HashMap[String, String] = this.headers): HttpURLConnection = {
    if (headers.nonEmpty) {
      val sanitizedHeaders: mutable.HashMap[String, String] = this.sanitizeHeaders(headers)
      sanitizedHeaders.foreach(kv => {
        connection.addRequestProperty(kv._1, kv._2)
        return connection
      })
    } else {
      return connection
    }
    connection
  }
}

// Testing here
object HandleHeaders extends App {
  val handleHeaders: HandleHeaders = new HandleHeaders()
  val headers: mutable.HashMap[String, String] = handleHeaders.sanitizeHeaders(mutable.HashMap("Accept" -> "*/*", "Accept-Encoding" -> "gzip", "Authorization" -> "<Credentials>"))
  println(headers)
}