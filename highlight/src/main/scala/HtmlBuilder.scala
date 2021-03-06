package lyrical.highlighter

trait HtmlBuilder { this : CssBuilder with BodyBuilder =>
  def css: String
  def body : String
  def build = "<html><head><style type='text/css'>"+css+"</style></head><body><pre>"+ body+ "</pre></body></html>"
}

trait CssBuilder{
  def css: String = ""
}

trait BodyBuilder{
  def body: String = ""
}


trait NormalCssBuilder extends CssBuilder{
  override def css: String = """
span.comment { color : #999999 } 
span.keyword { color : #0033FF } 
span.string  { color : #00AA33 } 
  """
}
