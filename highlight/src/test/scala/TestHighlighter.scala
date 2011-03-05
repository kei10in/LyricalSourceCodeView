import scala.util.parsing.input._

import org.scalatest.FunSuite

import lyrical.highlighter.Highlighter



class HighlighterTest extends FunSuite {
  
  test("interface test") {
    assert("\"Hello, World!\"" === Highlighter.highlight("\"Hello, World!\""))
  }

  test("line comment parser test") {
    assert(
"""<span class="comment">// comment</span><br>Hello, World!"""
      === Highlighter.highlight(
"""// comment
Hello, World!"""
      ))
  }

  test("block comment parser test") {
    assert(
"""<span class="comment">/* comment */</span>
Hello, World!"""
      === Highlighter.highlight(
"""/* comment */
Hello, World!"""
      ))
  }

}

