package lyrical.highlighter

import scala.util.parsing.combinator._


object ScalaTokenParsers extends RegexParsers {
  
  override def skipWhitespace = false

  val eof = "".r
  val rawChar = ".".r

  def rawParser: Parser[String] = rep(rawChar) ^^ {
    xs => xs.mkString
  }
}
