package Main

import HTTPSRequestsScala.Request

object RequestApp extends App {
  val requester: Request = new Request()

  // GET request, read-only
  val GET: String = requester.request("http://localhost:8080", "GET")
  println(GET)

  // POST, writable
  val POST: String = requester.request(
    "http://localhost:8080/echo",
    "POST",
    Map("Content-Type" -> "application/json; charset=UTF-8", "User-Agent" -> "Scala", "Accept" -> "application/json"),
    "{\"message\": \"POST message\"}"
  )
  println(POST)

  // DELETE, writable
  val DELETE: String = requester.request(
    "http://localhost:8080/echo",
    "DELETE",
    Map("Accept" -> "*/*", "User-Agent" -> "*")
  )
  println(DELETE)

  // PUT, writable
  val PUT: String = requester.request(
    "http://localhost:8080/echo",
    "PUT",
    Map("Accept" -> "*/*", "User-Agent" -> "*"),
    "{\"message\": \"PUT message\"}"
  )
  println(PUT)

  // PATCH, writable
  val PATCH: String = requester.request("http://localhost:8080/echo",
  "PATCH",
    Map("Accept" -> "*/*", "User-Agent" -> "*"),
    "{\"message\": \"PATCH message\"}")
  println(PATCH)

  // HEAD, read-only
  val HEAD: String = requester.request("http://httpbin.org/anything/head", "HEAD")
  println(HEAD)
}
