package OutputDestination

//a mock output handler for testing
class MockOutputHandler extends OutputHandler {
  private val _content = new StringBuilder()

  override def write(content: String): Unit = {
    _content.append(content)
    _content.append("\n")
  }

  def getContent: String = _content.toString().trim
}