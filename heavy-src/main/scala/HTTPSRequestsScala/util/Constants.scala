package HTTPSRequestsScala.util

/**
 * Created by KaNguy - 04/24/2021
 * File HTTPSRequestsScala.util.Constants.scala
 */

/**
 * HTTP-related constants
 * {{{
 *   import HTTPSRequestsScala.util.Constants
 *   val GET: String = Constants.GET
 * }}}
 */
object Constants {
  /**
   * Main HTTP/HTTPS methods
   */
  val GET: String = "GET"
  val POST: String = "POST"
  val DELETE: String = "DELETE"
  val PUT: String = "PUT"
  val HEAD: String = "HEAD"
  val OPTIONS: String = "OPTIONS"
  val PATCH: String = "PATCH"

  /**
   * Environment HTTP/HTTPS methods
   */
  val CONNECT: String = "CONNECT"
  val TRACE: String = "TRACE"

  /**
   * Officially supported methods
   */
  val HTTPMethods: Set[String] = Set(GET, POST, DELETE, PUT, HEAD, OPTIONS, TRACE)

  /**
   * Timeouts
   */
  val DEFAULT_TIMEOUT: Int = 5000
}
