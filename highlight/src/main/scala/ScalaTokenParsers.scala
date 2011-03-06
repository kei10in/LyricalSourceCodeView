package lyrical.highlighter

import scala.util.parsing.combinator._

// これがscalaのParser Combinator
object ScalaTokenParsers extends RegexParsers {
  // RegexParsersが空白を無視しない
  override def skipWhitespace = false

  val eof = "".r
  val anyChar = "(.|\n)".r

  def scalaParser: Parser[String] = rep(tokenParser) ^^ { xs => xs.mkString }

  def tokenParser: Parser[String] = commentParser | keywordParser | rawParser
  
  def rawParser: Parser[String] = anyChar 

  def commentParser: Parser[String] = lineCommentParser | blockCommentParser

  val lineComment = "//.*(?=[\r\n]+)".r
  def lineCommentParser: Parser[String] = lineComment ^^ {
    x => "<span class=\"comment\">" + x.takeWhile(_ != '\n').mkString + "</span>"
  }

  val blockComment = "/\\*(?:.|\n|\r)*?\\*/".r
  def blockCommentParser: Parser[String] = blockComment ^^ {
    x => "<span class=\"comment\">" + x + "</span>"
  }

  val importKeyword = "import"
  val packageKeyword = "package"
  val defKeyword = "def"
  val objectKeyword = "object"
  val classKeyword = "class"
  val traitKeyword = "trait"

  def keywordParser: Parser[String] = (
    importKeyword |
    packageKeyword |
    defKeyword |
    objectKeyword |
    classKeyword |
    traitKeyword
  ) ^^ {
    x => """<span class="keyword">""" + x + "</span>"
  }

  def stringLiteralParser =  (multiLineStringLiteral | singleLineStringLiteral) ^^ {
    x => """<span class="string">""" + x + "</span>"
  }
  def singleLineStringLiteral = '"' ~ rep(stringElement) ~ '"' ^^ {
    case q1 ~ xs ~ q2 => q1 + xs.mkString + q2
  }
 def multiLineStringLiteral = "\"\"\"" ~ multiLineChars ~ "\"\"\"" ~ not ('"') ^^ {
    case q1 ~ s ~ q2 ~ _ => q1 + s + q2
  }

  def stringElement = printableCharNoDoubleQuote | charEscapeSeq
  def printableCharNoDoubleQuote = """[^\a\t\n\v\f\r\e""]""".r
  def charEscapeSeq = """\[a-zA-Z""]""".r
  def multiLineChars: Parser[String] = "(?:(?:\"\"[^\"])|(?:\"[^\"])|(?:[^\"]))*\"*(?=\"\"\")".r

}
