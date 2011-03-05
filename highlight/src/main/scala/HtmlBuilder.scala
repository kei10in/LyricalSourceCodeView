package lyrical.highlighter

trait HtmlBuilder {
  def css: String
  def body : String
  def build = "<html><head><style type='text/css'>"+css+"</style></head><body>"+body.replace("\n", "<br/>") + "</body></html>"
}

trait CssBuilder{
  def css: String = ""
}

trait BodyBuilder{
  def body: String = ""
}


trait NormalCssBuilder{
  def css: String = """
span.comment { color : #999999 } 
  """
}
