package lyrical.highlighter

object Highlighter {
  
  import ScalaTokenParsers._
  
  def highlight(srcText: String) = ScalaTokenParsers.parseAll(rawParser, srcText).get

}
