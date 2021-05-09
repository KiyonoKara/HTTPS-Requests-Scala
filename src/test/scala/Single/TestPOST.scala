package Single

import HTTPSRequestsScala.Single.PostRequest

object TestPOST {
  def main(args: Array[String]): Unit = {
    val postRequest: PostRequest = new PostRequest("https://reqres.in/api/users")
    val post = postRequest.POST(
      "https://reqres.in/api/users",
      "{\"name\": \"Li Xi\", \"job\": \"Scala POST\"}",
            Map("Content-Type" -> "application/json; charset=UTF-8", "User-Agent" -> "Scala", "Accept" -> "application/json")
    )
    println(post)
  }
}
