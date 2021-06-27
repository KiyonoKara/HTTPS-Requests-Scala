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

  // Payload
  val POSTPayload: String = requester.request(
    "http://localhost:8080/payload",
    "POST",
    Map("Content-Type" -> "application/json; charset=UTF-8", "User-Agent" -> "Scala", "Accept" -> "application/json"),
    requester.collectionToJSON(
      Map("payload" ->
        Map("message" -> "This is a payload",
          "list" -> List(1, 2, 3, 4)
        ),
          "map" -> Map(
            "nested" -> List("One", "Two")
          )
      )
    )
  )
  println(POSTPayload)

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
    Map("Accept" -> "*/*", "User-Agent" -> "*", "X-HTTP-Method-Override" -> "PATCH"),
    "{\"message\": \"PATCH message\"}")
  println(PATCH)

  // HEAD, read-only
  val HEAD: String = requester.head("http://localhost:8080/echo")
  println(HEAD)

  // Quick methods
  val qPOST: String = requester.post("http://localhost:8080/echo", "{\"message\": \"Quick POST message\"}", headers = Map("Content-Type" -> "application/json; charset=UTF-8", "User-Agent" -> "Scala", "Accept" -> "application/json"))
  println(qPOST)

  // JSON parsing
  val parsedGET = requester.parseJSON(GET)
  val parsedPOST = requester.parseJSON(POST)
  val parsedPOSTPayload = requester.parseJSON(POSTPayload)
  val parsedDELETE = requester.parseJSON(DELETE)
  val parsedPUT = requester.parseJSON(PUT)
  val parsedPATCH = requester.parseJSON(PATCH)
  println(parsedGET + "\n" + parsedPOST + "\n" + parsedPOSTPayload + "\n" + parsedDELETE + "\n" + parsedPUT + "\n" + parsedPATCH)
  println(parsedGET.asInstanceOf[Map[Any, Any]].getOrElse("status", "status") + "\n" + parsedGET.asInstanceOf[Map[Any, Any]].getOrElse("status", "status").asInstanceOf[Map[Any, Any]].getOrElse("code", "message"))
}