import scala.util.parsing.input._

import org.scalatest.FunSuite

import lyrical.highlighter.Highlighter



class HighlighterTest extends FunSuite {
  
  test("interface test") {
    assert("\"Hello, World!\"" === Highlighter.highlight("\"Hello, World!\""))
  }

  test("line comment parser test") {
    assert(
"""<span class="comment">// comment</span>
Hello, World!"""
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

  test("block comment parser test with new line") {
    assert(
"""<span class="comment">/**
 * comment
 */</span>
Hello, World!"""
      === Highlighter.highlight(
"""/**
 * comment
 */
Hello, World!"""
      ))
  }


  test("keyword, def parsing") {
    val input = """def foo {
}
"""
    val expected = """<span class="keyword">def</span> foo {
}
"""
      assert(expected === Highlighter.highlight(input))
  }


  test("keyword, object parsing") {
    val input = """object foo {
}
"""
    val expected = """<span class="keyword">object</span> foo {
}
"""
      assert(expected === Highlighter.highlight(input))
  }


  test("keyword, class parsing") {
    val input = """class foo {
}
"""
    val expected = """<span class="keyword">class</span> foo {
}
"""
      assert(expected === Highlighter.highlight(input))
  }


  test("keyword, class parsing, but fail") {
    val input = """classfoo {
}
"""
    val expected = """classfoo {
}
"""
      assert(expected === Highlighter.highlight(input))
  }



}

