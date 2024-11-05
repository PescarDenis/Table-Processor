package Table

import Evaluation.EvaluationTypes.EvaluationResult
import Table.TableEntries.TableEntry
import File_Reader.CSVReader
//An interface for a table with multiple methods for accessing different configurations
trait TableInterface {
  // Retrieve a raw TableEntry (e.g., Number, Formula, or Empty) at a specific position
  def getCell(pos: ParseTableCells): TableEntry

  // Retrieve a full row of evaluated results by row index
  def getRow(rowIndex: Int): Map[ParseTableCells, EvaluationResult[_]]

  // Get the index of the last row or None if the table is empty
  def lastRow: Option[Int]

  // Get the index of the last column or None if the table is empty
  def lastColumn: Option[Int]

  // Return a collection of all positions of non-empty cells
  def nonEmptyPositions: Iterable[ParseTableCells]

  // Store an evaluated result for a specific cell position
  def storeEvaluatedResult(pos: ParseTableCells, result: EvaluationResult[_]): Unit

  // Retrieve an evaluated result as an EvaluationResult type for filtering or other purposes
  def getEvaluatedResult(pos: ParseTableCells): Option[EvaluationResult[_]]

  // Retrieve an evaluated result as a String for output purposes
  def getEvaluatedResultAsString(pos: ParseTableCells): String

  // Parse the CSV data into the table
  def parse(csvReader: CSVReader): TableInterface

}

