package OutputDestination

//base interface for the output destination
trait OutputHandler {
    def write(content : String) : Unit //takes the content to output and then handles it appropriately
}
//if we want the output to be easily extensible a factory class can be made in order to handle different destinations
