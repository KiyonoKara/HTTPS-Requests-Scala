import HTTPSRequestsScala.DeleteRequest

object TestDELETE {
  def main(args: Array[String]): Unit = {
    val deleteRequest: DeleteRequest = new DeleteRequest("https://reqbin.com/sample/delete/json")
    val output = deleteRequest.DELETE(deleteRequest.url, Array(Array("Accept", "*/*"), Array("User-Agent", "*")))
    println(output)
  }
}