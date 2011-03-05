import scala.util.parsing.input._

import org.scalatest.FunSuite

import lyrical.highlighter.Highlighter



class HighlighterTest extends FunSuite {
  
  test("interface test") {
    assert("Hello, World!" === Highlighter.highlight("Hello, World!"))
  }
}

