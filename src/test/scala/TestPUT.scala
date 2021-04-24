object TestPUT {
  def main(args: Array[String]): Unit = {
    val putRequest: PutRequest = new PutRequest("https://reqbin.com/echo/put/json")
    val put = putRequest.PUT("https://reqbin.com/echo/put/json", "{\"Id\":22222,\"Customer\":\"Ji Ji\",\"Quantity\":1,\"Price\":10}")
    println(put)
  }
}