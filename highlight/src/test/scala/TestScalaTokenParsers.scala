import scala.util.parsing.input._

import org.scalatest.FunSuite

import lyrical.highlighter.ScalaTokenParsers._


class ScalaTokenParsersTest extends FunSuite {
  
  test("string literal parsing test") {
    val input = "\"abc\""
    val expected = """<span class="string">"abc"</span>"""
    assert(expected === parseAll(stringLiteralParser, input).get)
  }

  test("multi line chars parsing test") {
    val input = "foo\"\"bar\"\"\""
    val expected = "foo\"\"bar\"\"\""
    assert(expected === parseAll(multiLineChars, input).get)
  }

  test("multi line string literal parsing test") {
    val input = "\"\"\"abc\"\"\""
    val expected = "<span class=\"string\">\"\"\"abc\"\"\"</span>"
    val actual = parseAll(stringLiteralParser, input) match {
      case Success(x, _) => x
      case Failure(msg, _) => msg
      case Error(msg, _) => msg
    }
    assert(expected === actual)
  }


}
