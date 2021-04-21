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
import scala.collection.immutable.HashMap

class HandleHeaders(var headers: mutable.HashMap[String, String] = mutable.HashMap.empty[String, String]) {
  /** Lower cases the headers by setting them all to lowercase, this is not recommended for authorization tokens and may invalidate it
   *
   * @param headers - HashMap of headers with the key and value
   * @return
   */
  def lowerCaseHeaders(headers: mutable.HashMap[String, String] = this.headers): mutable.HashMap[String, String] = {
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

  /** Adds headers that are in the form of HashMap
   *
   * @param connection - Needs the connection to add headers
   * @param headers - Requires the headers as a HashMap so it can be added to the connection headers
   */
  def addHeaders(connection: HttpURLConnection, headers: mutable.HashMap[String, String] = this.headers): Unit = {
    if (headers.nonEmpty) {
      val theHeaders: mutable.HashMap[String, String] = headers
      theHeaders.foreach(kv => {
        connection.addRequestProperty(kv._1, kv._2)
      })
    }
  }
}

// Testing here
object HandleHeaders extends App {
  val handleHeaders: HandleHeaders = new HandleHeaders()
  val headers: mutable.HashMap[String, String] = handleHeaders.lowerCaseHeaders(mutable.HashMap("Accept" -> "*/*", "Accept-Encoding" -> "gzip", "Authorization" -> "<Credentials>"))
  println(headers)
}