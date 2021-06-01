package HTTPSRequestsScala.util

class JSON {
  case class JSONException(JSON: String, throwable: Throwable = null) extends RuntimeException(s"Could not parse: $JSON", throwable)
  case class JSONObjectNotFound(JSONObjectName: String, throwable: Throwable) extends RuntimeException(s"""Could not find any JSON object named, "$JSONObjectName"""", throwable)
  case class MalformedJSONException(malformed: String, JSON: String) extends RuntimeException(s"""Due to $malformed, the data could not be parsed: $JSON""")
}
