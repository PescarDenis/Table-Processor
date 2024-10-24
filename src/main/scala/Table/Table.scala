package Table

import TableEntries._
import File_Reader.CSVReader
import Evaluation.EvaluationTypes.EvaluationResult

class Table extends TableInterface {
  private var rows: Map[ParseTableCells, TableEntry] = Map()
  private var evaluatedResults: Map[ParseTableCells, EvaluationResult[_]] = Map() // Separate map to store evaluated results

  // Method to retrieve the raw TableEntry
  def getCell(pos: ParseTableCells): TableEntry = {
    rows.getOrElse(pos, Empty(pos.row, pos.col))
  }

  // Method to store the evaluated result
  def storeEvaluatedResult(pos: ParseTableCells, result: EvaluationResult[_]): Unit = {
    evaluatedResults += (pos -> result)
  }

  // Method to get the evaluated result if it exists
  def getEvaluatedResult(pos: ParseTableCells): Option[EvaluationResult[_]] = {
    evaluatedResults.get(pos)
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

  // Parse the CSV data into the table
  def parse(csvReader: CSVReader): Table = {
    rows = csvReader.zipWithIndex.flatMap { case (row, rowIndex) =>
      // For each row, iterate through the columns (cell values)
      row.zipWithIndex.map { case (cellValue, colIndex) =>
        val cellPos = ParseTableCells(rowIndex + 1, colIndex + 1)
        val entry = parseCell(cellValue, rowIndex + 1, colIndex + 1) // Parse each cell
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
        val numberEntry = Number(rowIndex, colIndex)
        numberEntry.set(cellValue) // Call the set method to populate numberValue
        numberEntry // Return the Number entry
      } catch {
        case _: NumberFormatException =>
          Empty(rowIndex, colIndex)
      }
    }
  }
}
