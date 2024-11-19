package Filters

//provide an error class to propagate the filtering errors that the users encounter
class FilterError(message: String) extends Exception(message) {
    println(s"Filter Error: $message")
}
