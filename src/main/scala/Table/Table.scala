package Table

import TableEntries._
import File_Reader.CSVReader
class Table extends TableInterface {
  private var rows: Map[ParseTableCells, TableEntry] = Map()

  def getRows: Map[ParseTableCells, TableEntry] = rows
  // Retrieve a cell at a given position
  override def getCell(pos: ParseTableCells): TableEntry = {
    rows.getOrElse(pos, Empty(pos.row, pos.col))
  }

  // Get the index of the last row, or None if the table is empty
  override def lastRow: Option[Int] = {
    if (rows.isEmpty) None else rows.keys.map(_.row).maxOption
  }

  // Get the index of the last column, or None if the table is empty
  override def lastColumn: Option[Int] = {
    if (rows.isEmpty) None else rows.keys.map(_.col).maxOption
  }

  // Return a collection of all positions of non-empty cells
  override def nonEmptyPositions: Iterable[ParseTableCells] = {
    rows.keys
  }

  def parse(csvReader: CSVReader): Table = {
    // Iterate through the rows returned by csvReader
    rows = csvReader.zipWithIndex.flatMap { case (row, rowIndex) =>
      // For each row, iterate through the columns (cell values)
      row.zipWithIndex.map { case (cellValue, colIndex) =>
        val cellPos = ParseTableCells(rowIndex+1, colIndex+1)
        val entry = parseCell(cellValue, rowIndex+1, colIndex+1) // Parse each cell
        cellPos -> entry // Store the position and the parsed entry in the map
      }
    }.toMap // Convert the sequence of tuples to a map
    this // Return the current table instance
  }

  // Helper method to parse individual cells into TableEntry objects
  private def parseCell(cellValue: String, rowIndex: Int, colIndex: Int): TableEntry = {
    if (cellValue.trim.isEmpty) {
       Empty(rowIndex, colIndex)
    } else if (cellValue.startsWith("=")) {
      val formulaEntry = new Formula(rowIndex, colIndex)
      formulaEntry.set(cellValue) // Call the set method to populate the formula
      formulaEntry
    } else {
      try {
        val numberEntry =  Number(rowIndex, colIndex)
        numberEntry.set(cellValue) // Call the set method to populate numberValue
        numberEntry // Return the Number entry
      } catch {
        case _: NumberFormatException =>
           Empty(rowIndex, colIndex)
      }
    }
  }
}
