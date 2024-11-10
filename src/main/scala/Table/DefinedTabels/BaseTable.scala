package Table.DefinedTabels

import Evaluation.EvaluationResult
import Evaluation.EvaluationTypes._
import File_Reader.CSVReader
import Table.TableInterface
import TableParser.{FileParser, ParseTableCells}
import Table.TableEntries.{Empty, TableEntry}
import ExpressionAST.EvaluationContext
import Evaluation.TableEvaluator
class BaseTable(parser: FileParser) extends TableInterface {
  private var rows: Map[ParseTableCells, TableEntry] = Map()
  private var evaluatedResults: Map[ParseTableCells, EvaluationResult[?]] = Map()
  
  
  override def initializeRows(parsedRows: Map[ParseTableCells, TableEntry]): Unit = 
    rows = parsedRows //used after we get the parsed table
  
  override def getCell(pos: ParseTableCells): TableEntry =
    rows.getOrElse(pos, Empty(pos.row, pos.col))
  

  override def getRow(rowIndex: Int): Map[ParseTableCells, EvaluationResult[?]] =
    evaluatedResults.filter { case (cell, _) => cell.row == rowIndex }

  override def lastRow: Option[Int] =
    rows.keys.map(_.row).maxOption

  override def lastColumn: Option[Int] =
    rows.keys.map(_.col).maxOption

  override def nonEmptyPositions: Iterable[ParseTableCells] = {
    evaluatedResults.collect {
      case (pos, result) if !result.isInstanceOf[EmptyResult.type] => pos
    }.toList
  }
  override def getRawEntires: Iterable[ParseTableCells] = rows.keys //get all the raw entries
  
  override def storeEvaluatedResult(pos: ParseTableCells, result: EvaluationResult[?]): Unit =
    evaluatedResults += (pos -> result)

  override def getEvaluatedResult(pos: ParseTableCells): Option[EvaluationResult[?]] =
    evaluatedResults.get(pos)

  override def getEvaluatedResultAsString(pos: ParseTableCells): String = {
    evaluatedResults.get(pos).map {
      case IntResult(value) => value.toString
      case FloatResult(value) => value.toString
      case EmptyResult => ""
      case _ => "ERROR"
    }.getOrElse(" ") //convert each result to its string implementation
  }

  override def parse(csvReader: CSVReader): Unit =
    initializeRows(parser.parse(csvReader)) // Decoupled parsing and initialization

  override def evaluateAllCells(context: EvaluationContext): Unit = {
    val evaluator = new TableEvaluator(this, context)
    evaluator.evaluateAllCellsAndStoreResults()
  }
}
