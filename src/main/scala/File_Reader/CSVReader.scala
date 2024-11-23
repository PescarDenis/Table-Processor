package File_Reader

import scala.io.Source

//Now we are going to build a class to read the CSV file
//The CSVReader has 2 constructors, the input source file
//and the separator,
class CSVReader(input: Source ,  separator: CSVSeparator) extends CSVIterator {
  //first we are going to want to filter all the rows which have only empty cells
  private val rows = input.getLines().filter(_.trim.nonEmpty) ///ensures that we are only
  //reading the rows in the file which contain actual data

  override def hasNext: Boolean = rows.hasNext //check if there are any rows left

  override def next(): List[String] = {
    val row = rows.next()
    row.split(separator.CellSeparator).map(_.trim).toList
    //which has a regular expression as a parameter and convert the splitted array into a list
    // as split returns an array
  }
}
