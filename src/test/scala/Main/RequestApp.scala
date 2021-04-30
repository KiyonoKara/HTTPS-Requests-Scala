package Main

import HTTPSRequestsScala.Request

object RequestApp extends App {
  val requester: Request = new Request()

  // GET request, read-only
  val GET: String = requester.request("https://docs.scala-lang.org", "GET")
  println(GET)

  // POST, writable
  val POST: String = requester.request(
    "https://reqres.in/api/users",
    "POST",
    Array(Array("Content-Type", "application/json; charset=UTF-8"), Array("User-Agent", "Scala"), Array("Accept", "application/json")),
    "{\"name\": \"Li Xi\", \"job\": \"Scala POST\"}"
  )
  println(POST)

  // DELETE, writable
  val DELETE: String = requester.request(
    "https://reqbin.com/sample/delete/json",
    "DELETE",
    Array(Array("Accept", "*/*"), Array("User-Agent", "*"))
  )
  println(DELETE)

  // PUT, writable
  val PUT: String = requester.request(
    "https://reqbin.com/echo/put/json",
    "PUT",
    Array(Array("Accept", "*/*"), Array("User-Agent", "*")),
    "{\"Id\":999,\"Customer\":\"Ji Ji\",\"Quantity\":1,\"Price\":10}"
  )
  println(PUT)
}
