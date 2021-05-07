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
    Array(Array("Content-Type", "application/json; charset=UTF-8"), Array("User-Agent", "Scala"), Array("Accept", "application/json")),
    "{\"message\": \"POST message\"}"
  )
  println(POST)

  // DELETE, writable
  val DELETE: String = requester.request(
    "http://localhost:8080/echo",
    "DELETE",
    Array(Array("Accept", "*/*"), Array("User-Agent", "*"))
  )
  println(DELETE)

  // PUT, writable
  val PUT: String = requester.request(
    "http://localhost:8080/echo",
    "PUT",
    Array(Array("Accept", "*/*"), Array("User-Agent", "*")),
    "{\"message\": \"PUT message\"}"
  )
  println(PUT)
}
