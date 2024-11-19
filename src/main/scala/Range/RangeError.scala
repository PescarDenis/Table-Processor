package Range

class RangeError(message: String) extends Exception(message) {
  def logError(): Unit = {
    println(s"Range Error: $message")
  }
}

