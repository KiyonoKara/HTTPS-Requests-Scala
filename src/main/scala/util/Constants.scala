package util

/**
 * Created by KaNguy - 04/24/2021
 * File util.Constants.scala
 */

/**
 * HTTP-related constants
 * {{{
 *   import util.Constants
 *   val GET: String = Constants.GET
 * }}}
 */
object Constants {
  /**
   * Supported methods
   */
  /** Read-only; Represents full resource and is meant for only getting data */
  val GET: String = "GET"
  /** Read-only; Asks for a response without the body or data */
  val HEAD: String = "HEAD"
  /** Writable; Used for submitting entities, bodies, or data, usually results in additions */
  val POST: String = "POST"
  /** Writable; Replaces target with the requests's data or payload */
  val PUT: String = "PUT"
  /** Writable; Removes a resource or entity; Many APIs will require an authorization header along with this kind of request */
  val DELETE: String = "DELETE"
  /** Writable; Applies partial modifications to a resource or entity */
  val PATCH: String = "PATCH"


  /**
   * Unsupported methods
   */
  /** Read-only; Returns 'Allow' headers, CORS and maybe not practical */
  val OPTIONS: String = "OPTIONS"
  /** Connection; Tries to establish an HTTP tunnel, mainly for SSL connections */
  val CONNECT: String = "CONNECT"

  /**
   * Timeouts
   */
  val DEFAULT_TIMEOUT: Int = 5000
}
