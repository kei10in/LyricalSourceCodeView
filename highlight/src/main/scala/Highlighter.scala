package lyrical.highlighter

object Highlighter {
  
  import ScalaTokenParsers._
  
  def highlight(srcText: String) = ScalaTokenParsers.parseAll(scalaParser, srcText) match {
    case Success(x, _) => x
    case Failure(msg, _) => msg
    case Error(msg, _) => msg
  }

  

}
