package Table
import TableEntries.TableEntry
import File_Reader.{CSVReader,CSVSeparator}
import scala.io.Source
///a base interface to manage the tabular data containing TableEntries
trait TableInterface {
  def getCell(pos : ParseTableCells) : TableEntry //takes the position of the cell and returns what we get in that cell
  def lastRow : Option[Int] ///returns the index of the last row or None if the table is empty
  def lastColumn : Option[Int] ///returns the index of the las column or None if there are no columns
  def nonEmptyPositions : Iterable[ParseTableCells] //returns a collection of all positions of nonempty cells
}
dadwadawdawda