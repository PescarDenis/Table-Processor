package FileOutputHandlerTest

import org.scalatest.funsuite.AnyFunSuite
import java.nio.file.{Files, Path}
import java.nio.charset.StandardCharsets
import scala.io.Source
import OutputDestination.FileOutputHandler

//Testing to see if indeed the FileHandler will write some content to a file
class FileOutputHandlerTest extends AnyFunSuite {

  test("FileOutputHandler should write content to a file") {
    //create a temporary file
    val tempFile: Path = Files.createTempFile("testOutput", ".txt")
    val filepath = tempFile.toString

      //Create an instance of the fileoutputhandler
      val handler = new FileOutputHandler(filepath)

      //Test content
      val content = "I am trying to write something to the file does not really matter what\nYesYEsYesNONONONO"

      //Write content to the file
      handler.write(content)

      //Read back the content to check correctness
      val fileContent = Source.fromFile(filepath, StandardCharsets.UTF_8.name()).getLines().mkString("\n").trim
      assert(fileContent == content)
  }
}
