package Single

import HTTPSRequestsScala.Single.PostRequest
import HTTPSRequestsScala.utility.Utility

object TestPOST {
  def main(args: Array[String]): Unit = {
    val postRequest: PostRequest = new PostRequest("https://reqres.in/api/users")
    val post = postRequest.POST(
      "https://reqres.in/api/users",
      "{\"name\": \"Li Xi\", \"job\": \"Scala POST\"}",
            Map("Content-Type" -> "application/json; charset=UTF-8", "User-Agent" -> "Scala", "Accept" -> "application/json")
    )
    println(post)

    val postReq2: PostRequest = new PostRequest("https://reqres.in/api/users")
    val post2 = postReq2.POST(
      "https://reqres.in/api/users",
      Utility.singleMapToJSON(Map("name"-> "Li Xi", "job" -> "Scala POST")),
      Map("Content-Type" -> "application/json; charset=UTF-8", "User-Agent" -> "Scala", "Accept" -> "application/json")
    )
    println(post2)
  }
}
