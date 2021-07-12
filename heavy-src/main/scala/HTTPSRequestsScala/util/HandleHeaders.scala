package HTTPSRequestsScala.utility

/**
 * Created by KaNguy - 05/07/2021
 * File HTTPSRequestsScala.util.HandleHeaders.scala
 */

// Networking & Web
import java.net.HttpURLConnection

object HandleHeaders {
  def setHeaders(connection: HttpURLConnection, headers: Iterable[(String, String)] = Nil): Unit = {
    if (headers.nonEmpty) {
      headers foreach {
        case (key, value) =>
          try {
            connection.setRequestProperty(key, value)
          } catch {
            case _: Any => ()
          }
      }
    }
  }

  def addHeaders(connection: HttpURLConnection, headers: Iterable[(String, String)] = Nil): Unit = {
    if (headers.isEmpty) { return }
    headers foreach {
      case (key, value) =>
        try {
          connection.addRequestProperty(key, value)
        } catch {
          case _: Any => ()
        }
    }
  }
}
