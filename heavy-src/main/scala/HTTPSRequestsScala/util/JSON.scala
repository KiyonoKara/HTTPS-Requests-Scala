package HTTPSRequestsScala.utility

// Scala Annotations
import scala.annotation.tailrec

object JSON {
  def parse(json: String): Any = {
    JSONParser.parse(json)
  }

  private trait Token {
    def value: String
  }

  private object Token {
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

  private object Tokenizer {
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

    private class TokenMatcher(partialRegex: String) {
      private val regex = ("^(" + partialRegex + ")").r

      def unapply(string: String): Option[String] = regex.findFirstIn(string)
    }

    object TokenMatcher {
      def apply(partialRegex: String) = new TokenMatcher(partialRegex)
    }

    def tokenize(json: String, tokens: List[Token] = List()): List[Token] = {
      val trimmedJSON = json.trim.replaceAll("\\s+", "")

      def continue(token: Token): List[Token] = {
        tokenize(trimmedJSON.substring(token.value.length), token :: tokens)
      }

      trimmedJSON match {
        case "" => tokens.reverse
        case LeftCurlyBrace(str) => continue(Token.LEFT_CURLY_BRACE)
        case RightCurlyBrace(str) => continue(Token.RIGHT_CURLY_BRACE)
        case LeftSquareBracket(str) => continue(Token.LEFT_SQUARE_BRACKET)
        case RightSquareBracket(str) => continue(Token.RIGHT_SQUARE_BRACKET)
        case Colon(str) => continue(Token.COLON)
        case Comma(str) => continue(Token.COMMA)
        case IsTrue(str) => continue(Token.TRUE)
        case IsFalse(str) => continue(Token.FALSE)
        case IsNull(str) => continue(Token.NULL)
        case String(str) => continue(Token.StringToken(str))
        case Number(str) => continue(Token.NumberToken(str))
        case error => println(s"""Could not complete action: $error"""); tokens.reverse
      }
    }
  }

  object JSONParser {

    def parse(json: String): Any = parse(Tokenizer.tokenize(json))

    private def parse(tokens: List[Token]): Any = tokens match {
      case Token.LEFT_CURLY_BRACE :: _ => jsonObject(tokens)
      case Token.LEFT_SQUARE_BRACKET :: _ => jsonArray(tokens)
      case _ => throw JSONException(toJsonString(tokens))

    }

    private def jsonObject(tokens: List[Token]): Map[String, Any] = {
      if (tokens.last != Token.RIGHT_CURLY_BRACE) {
        throw MalformedJSONException("JSON is missing a closing '}'", toJsonString(tokens))
      }

      def objectContent(tokens: List[Token]): Map[String, Any] = {
        tokens match {
          case (key: Token.StringToken) :: Token.COLON :: aValue :: Nil => Map(key.toString -> value(aValue))
          case (key: Token.StringToken) :: Token.COLON :: aValue :: Token.COMMA :: more => Map(key.toString -> value(aValue)) ++ objectContent(more)
          case (key: Token.StringToken) :: Token.COLON :: Token.LEFT_CURLY_BRACE :: more =>
            val (objectTokens, rest) = takeJsonObjectFromHead(Token.LEFT_CURLY_BRACE :: more)
            Map(key.toString -> value(objectTokens)) ++ objectContent(rest)
          case (key: Token.StringToken) :: Token.COLON :: Token.LEFT_SQUARE_BRACKET :: more =>
            val (arrayTokens, rest) = takeJsonArrayFromHead(Token.LEFT_SQUARE_BRACKET :: more)
            Map(key.toString -> value(arrayTokens)) ++ objectContent(rest)
          case Nil => Map()
          case _ => throw MalformedJSONException("Error", toJsonString(tokens))
        }
      }
      objectContent(tokens.tail.init)
    }

    private def jsonArray(tokens: List[Token]): List[Any] = {
      if (tokens.last != Token.RIGHT_SQUARE_BRACKET) {
        throw MalformedJSONException("JSON is missing a closing ']'", toJsonString(tokens))
      }

      def arrayContents(tokens: List[Token]): List[Any] = {
        tokens match {
          case aValue :: Token.COMMA :: rest => value(aValue) :: arrayContents(rest)
          case aValue :: Nil => value(aValue) :: Nil
          case Token.LEFT_CURLY_BRACE :: _ =>
            val (objectTokens, rest) = takeJsonObjectFromHead(tokens)
            value(objectTokens) :: arrayContents(rest)
          case Token.LEFT_SQUARE_BRACKET :: _ =>
            val (arrayTokens, rest) = takeJsonArrayFromHead(tokens)
            value(arrayTokens) :: arrayContents(rest)
          case Nil => List()
          case _ => throw MalformedJSONException("Error", toJsonString(tokens))
        }
      }
      arrayContents(tokens.tail.init)
    }

    private def takeJsonArrayFromHead(tokens: List[Token]): (List[Token], List[Token]) = {
      splitAtMatchingTokenPair((Token.LEFT_SQUARE_BRACKET, Token.RIGHT_SQUARE_BRACKET), tokens.indexOf(Token.RIGHT_SQUARE_BRACKET), tokens)
    }

    private def takeJsonObjectFromHead(tokens: List[Token]): (List[Token], List[Token]) = {
      splitAtMatchingTokenPair((Token.LEFT_CURLY_BRACE, Token.RIGHT_CURLY_BRACE), tokens.indexOf(Token.RIGHT_CURLY_BRACE), tokens)
    }

    @tailrec
    private def splitAtMatchingTokenPair(tokenPair: (Token, Token), indexOfNextClosingToken: Int, tokens: List[Token]): (List[Token], List[Token]) = {
      val (possibleObject, rest) = tokens.splitAt(indexOfNextClosingToken + 1)
      if (possibleObject.count(_ == tokenPair._1) != possibleObject.count(_ == tokenPair._2)) {
        splitAtMatchingTokenPair(tokenPair, tokens.indexOf(tokenPair._2, indexOfNextClosingToken + 1), tokens)
      } else {
        (
          possibleObject, if (rest.headOption.contains(Token.COMMA)) {
            rest.tail
          } else {
            rest
          }
        )
      }

    }

    private def value(token: Token): Any = value(List(token))

    private def value(tokens: List[Token]): Any = {
      tokens match {
        case (value: Token.StringToken) :: Nil => value.toString()
        case Token.NumberToken(number) :: Nil => BigDecimal(number)
        case Token.LEFT_CURLY_BRACE :: _ => jsonObject(tokens)
        case Token.LEFT_SQUARE_BRACKET :: _ => jsonArray(tokens)
        case Token.TRUE :: Nil => true
        case Token.FALSE :: Nil => false
        case Token.NULL :: Nil => null
        case _ => throw MalformedJSONException("Error", toJsonString(tokens))
      }
    }

    private def toJsonString(tokens: List[Token]) = tokens.map(_.value).mkString
  }

  private case class JSONException(JSON: String, throwable: Throwable = null) extends RuntimeException(s"Could not parse: $JSON", throwable)
  private case class JSONObjectNotFound(JSONObjectName: String, throwable: Throwable) extends RuntimeException(s"""Could not find any JSON object named, "$JSONObjectName"""", throwable)
  private case class MalformedJSONException(malformed: String, JSON: String) extends RuntimeException(s"""Due to $malformed, the data could not be parsed: $JSON""")
}
