package OutputDestination

class StdoutOutputHandler extends OutputHandler {
  override def write(content: String): Unit = {
    println(content)
  }
}
