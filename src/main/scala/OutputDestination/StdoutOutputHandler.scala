package OutputDestination

//just prints the table into STDOUT
class StdoutOutputHandler extends OutputHandler {
  override def write(content: String): Unit = {
    println(content)
  }
}
