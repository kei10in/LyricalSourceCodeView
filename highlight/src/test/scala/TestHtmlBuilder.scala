import scala.util.parsing.input._

import org.scalatest.FunSuite
import lyrical.highlighter._


class HtmlBuilderTest extends FunSuite {
  
  test("make empty Html") {
    val builder = new HtmlBuilder with CssBuilder with BodyBuilder
    assert("<html><head><style type='text/css'></style></head><body><pre></pre></body></html>" === builder.build)
  }

  test("make ") {
    val builder = new HtmlBuilder with CssBuilder with BodyBuilder {
      override def body = Highlighter.highlight("", "abc")
    }


    assert("<html><head><style type='text/css'></style></head><body><pre>abc</pre></body></html>" 
	   === builder.build)
  }

}

