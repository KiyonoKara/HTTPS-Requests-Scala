package HTTPSRequestsScala

/**
 * Created by KaNguy - 04/26/2021
 * File HTTPSRequestsScala.Request.scala
 */

// Data streaming and serialization
import java.io.{InputStreamReader, Reader}

// Networking and web
import java.net.{HttpURLConnection, URL}

// Scala IO Source
import scala.io.Source.{fromInputStream, fromURL}

// Utils
import java.util.zip.GZIPInputStream

// Local utilities
import util.{Constants, Convert, HandleHeaders}

class Request(var url: String, var method: String = "GET", headers: Array[Array[String]] = Array[Array[String]]()) {
  def request(url: String = this.url, method: String = this.method, headers: Array[Array[String]] = this.headers, data: String = null): String = {
    if (method.toUpperCase.equals(Constants.GET)) {

    }
    ""
  }
  // TODO: Finish this by this weekend? (No ETA)
}
