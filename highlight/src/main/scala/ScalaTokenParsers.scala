package lyrical.highlighter

import scala.util.parsing.combinator._


object ScalaTokenParsers extends RegexParsers {
  
  override def skipWhitespace = false

  val eof = "".r
  val anyChar = "(.|\n)".r

  def scalaParser: Parser[String] = rep(tokenParser) ^^ { xs => xs.mkString }

  def tokenParser: Parser[String] = commentParser | rawParser

  def rawParser: Parser[String] = anyChar 

  def commentParser: Parser[String] = lineCommentParser | blockCommentParser

  val lineComment = "//.*\n".r
  def lineCommentParser: Parser[String] = lineComment ^^ {
    case x => "<span class=\"comment\">" + x.takeWhile(_ != '\n') + "</span><br>"
  }

  val blockComment = """/\*.*?\*/""".r
  def blockCommentParser: Parser[String] = blockComment ^^ {
    case x => "<span class=\"comment\">" + x.replace("\n", "<br>") + "</span>"
  }
  
}
