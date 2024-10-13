package File_Reader_Test

import File_Reader.{CSVReader, CSVSeparator}
import scala.io.Source
import org.scalatest.funsuite.AnyFunSuite


class CSVReaderTest extends AnyFunSuite {

  test("Test1"){
    val table = "Dog,Cat,Me,Something,Aiaiaai,Idk,I\nlove\nOOP" ///input table

    val reader = new CSVReader(Source.fromString(table), CSVSeparator(",")) ///the actual reader

    val list=reader.toList ///make the reader into a List

    assert(list ==List(
      List("Dog","Cat","Me","Something","Aiaiaai","Idk","I"),
      List("love"),
      List("OOP")
    ))
    
  }

  test("Test2") {
    val table = "1;2;3\n4;5;6\nye;ba;na" ///input table

    val reader = new CSVReader(Source.fromString(table), CSVSeparator(";")) ///the actual reader

    val list = reader.toList ///make the reader into a List

    assert(list == List(
      List("1","2","3"),
      List("4","5","6"),
      List("ye","ba","na")
    ))

  }

  test("Test3") {
    val table = "  " ///an empty table,

    val reader = new CSVReader(Source.fromString(table), CSVSeparator(";")) ///the actual reader
    ///the separator does not matter as the table is empty and we filtered the empty lines

    val list = reader.toList ///make the reader into a List

    assert(list == List())

  }

}
