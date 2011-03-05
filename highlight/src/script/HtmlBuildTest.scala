import lyrical.highlighter._

val str = Highlighter.buildHtml("""
package lyrical.highlighter

object Highlighter {
  
  import ScalaTokenParsers._
  
//  def highlight(srcText: String):String = highlight("", srcText)

  def highlight(filename: String, srcText: String):String = ScalaTokenParsers.parseAll(scalaParser, srcText) match {
    case Success(x, _) => x
    case Failure(msg, _) => msg
    case Error(msg, _) => msg
  }
/*
  def buildHtml(srcText: String): String = buildHtml("", srcText)

  def buildHtml(filename: String, srcText: String): String = {
    val builder = new HtmlBuilder with CssBuilder with BodyBuilder {
      override def body = highlight(filename, srcText)
    }

    builder.build
  }
*/
}
""")


//println(str)
