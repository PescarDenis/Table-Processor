package OutputDestination
import java.io.{FileWriter,BufferedWriter,File}
//a class that provides the table to be written into a file
//filepath is the path where the content will be written
class FileOutputHandler(filepath : String) extends OutputHandler {
  private val resolvedFile = new File(filepath).getAbsoluteFile
  override def write(content: String): Unit = {
      val writer = new BufferedWriter(new FileWriter(resolvedFile)) //wraps the filewriter in a buffer to buffer the output
      try{
        writer.write(content) //write the content to the file
        writer.flush()
      }finally{
        writer.close() //close the file regarding if we occur an error
      }
       println(s"Output written to file: $resolvedFile")
  }
}
