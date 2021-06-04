package Single

import HTTPSRequestsScala.Single.DeleteRequest

object TestDELETE {
  def main(args: Array[String]): Unit = {
    val deleteRequest: DeleteRequest = new DeleteRequest("https://reqbin.com/sample/delete/json")
    val output = deleteRequest.DELETE(deleteRequest.url, Map("Accept" -> "*/*", "User-Agent" -> "*"))
    println(output)
  }
}
