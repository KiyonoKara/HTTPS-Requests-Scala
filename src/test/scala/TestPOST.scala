import HTTPSRequestsScala.PostRequest

object TestPOST {
  def main(args: Array[String]): Unit = {
    val postRequest: PostRequest = new PostRequest("https://reqres.in/api/users")
    val post = postRequest.POST(
      "https://reqres.in/api/users",
      "{\"name\": \"Li Xi\", \"job\": \"Scala POST\"}",
            Array(Array("Content-Type", "application/json; charset=UTF-8"), Array("User-Agent", "Scala"), Array("Accept", "application/json"))
    )
    println(post)
  }
}
