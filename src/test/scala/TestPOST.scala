object TestPOST {
  def main(args: Array[String]): Unit = {
    val postRequest: PostRequest = new PostRequest("https://reqres.in/api/users", "{\"name\": \"Li Xi\", \"job\": \"Scala POST\"}")
    val post = postRequest.POST()
    println(post)
  }
}
