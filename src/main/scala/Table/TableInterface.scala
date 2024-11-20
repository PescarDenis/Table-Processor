package Table

import Evaluation.EvaluationResult
import ExpressionAST.EvaluationContext
import File_Reader.CSVReader
import Table.TableEntries.*
import TableParser.ParseTableCells
import Filters.Row
// A Table interface that defines some basic methods of getting and some setting for a table 
trait TableInterface {

  def getCell(pos: ParseTableCells): TableEntry

  def getRow(rowIndex: Int): Row

  def lastRow: Option[Int]

  def lastColumn: Option[Int]

  def nonEmptyPositions: Iterable[ParseTableCells]

  def getRawEntires: Iterable[ParseTableCells]

  def storeEvaluatedResult(pos: ParseTableCells, result: EvaluationResult[?]): Unit

  def getEvaluatedResult(pos: ParseTableCells): Option[EvaluationResult[?]]

  def getEvaluatedResultAsString(pos: ParseTableCells): String

  def evaluateAllCells(context: EvaluationContext): Unit

  def parse(csvReader: CSVReader): Unit

  def initializeRows(parsedRows: TableModel[TableEntry]): Unit
}
