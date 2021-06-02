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

  object Tokenizer {
    val LeftCurlyBrace: TokenMatcher = TokenMatcher("\\{")
    val RightCurlyBrace: TokenMatcher = TokenMatcher("\\}")
    val LeftSquareBracket: TokenMatcher = TokenMatcher("\\[")
    val RightSquareBracket: TokenMatcher = TokenMatcher("\\]")
    val Colon: TokenMatcher = TokenMatcher(":")
    val Comma: TokenMatcher = TokenMatcher(",")
    val IsTrue: TokenMatcher = TokenMatcher("true")
    val IsFalse: TokenMatcher = TokenMatcher("false")
    val IsNull: TokenMatcher = TokenMatcher("null")
    val String: TokenMatcher = TokenMatcher("\".*?\"")
    val Number: TokenMatcher = TokenMatcher("[-+]?[0-9]*[\\,\\.]?[0-9]+([eE][-+]?[0-9]+)?")

    class TokenMatcher(partialRegex: String) {
      private val regex = ("^(" + partialRegex + ")").r

      def unapply(string: String): Option[String] = regex.findFirstIn(string)
    }

    object TokenMatcher {
      def apply(partialRegex: String) = new TokenMatcher(partialRegex)
    }

    def tokenize(json: String, tokens: List[Token] = List()): List[Token] = {
      val trimmedJSON = json.trim
      def continue(token: Token): List[Token] = {
        tokenize(trimmedJSON.substring(token.value.length), token :: tokens)
      }

      trimmedJSON match {
        case "" => tokens.reverse
        case LeftCurlyBrace(s) => continue(Token.LEFT_CURLY_BRACE)
        case RightCurlyBrace(s) => continue(Token.RIGHT_CURLY_BRACE)
        case LeftSquareBracket(s) => continue(Token.LEFT_SQUARE_BRACKET)
        case RightSquareBracket(s) => continue(Token.RIGHT_SQUARE_BRACKET)
        case Colon(s) => continue(Token.COLON)
        case Comma(s) => continue(Token.COMMA)
        case IsTrue(s) => continue(Token.TRUE)
        case IsFalse(s) => continue(Token.FALSE)
        case IsNull(s) => continue(Token.NULL)
        case String(s) => continue(Token.StringToken(s))
        case Number(s) => continue(Token.NumberToken(s))
        case error => println(s"""Could not complete action: $error"""); tokens.reverse
      }
    }
  }

  case class JSONException(JSON: String, throwable: Throwable = null) extends RuntimeException(s"Could not parse: $JSON", throwable)
  case class JSONObjectNotFound(JSONObjectName: String, throwable: Throwable) extends RuntimeException(s"""Could not find any JSON object named, "$JSONObjectName"""", throwable)
  case class MalformedJSONException(malformed: String, JSON: String) extends RuntimeException(s"""Due to $malformed, the data could not be parsed: $JSON""")
}
