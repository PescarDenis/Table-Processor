package OutputDestination

//base interface for the output destination
trait OutputHandler {
    def write(content : String) : Unit //takes the content to output and then handles it appropriately
}
