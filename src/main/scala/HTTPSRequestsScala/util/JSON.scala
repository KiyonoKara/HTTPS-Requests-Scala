package HTTPSRequestsScala.util

class JSON {
  trait Token {
    def value: String
  }
  object Token {
    case object LEFT_CURLY_BRACE extends Token { val value: String = "{" }
    case object RIGHT_CURLY_BRACE extends Token { val value: String = "}" }
    case object LEFT_SQUARE_BRACKET extends Token { val value: String = "[" }
    case object RIGHT_SQUARE_BRACKET extends Token { val value: String = "]" }
    case object COLON extends Token { val value: String = ":" }
    case object COMMA extends Token { val value: String = "," }
    case object TRUE extends Token { val value: String = "true" }
    case object FALSE extends Token { val value: String = "false" }
    case object NULL extends Token { val value: String = "null" }
    case class NumberToken(value: String) extends Token
    case class StringToken(value: String) extends Token {
      override def toString: String = value.tail.init
    }
  }



  case class JSONException(JSON: String, throwable: Throwable = null) extends RuntimeException(s"Could not parse: $JSON", throwable)
  case class JSONObjectNotFound(JSONObjectName: String, throwable: Throwable) extends RuntimeException(s"""Could not find any JSON object named, "$JSONObjectName"""", throwable)
  case class MalformedJSONException(malformed: String, JSON: String) extends RuntimeException(s"""Due to $malformed, the data could not be parsed: $JSON""")
}
