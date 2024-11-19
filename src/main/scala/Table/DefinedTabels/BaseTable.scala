package Table.DefinedTabels

import Evaluation.EvaluationResult
import Evaluation.EvaluationTypes._
import File_Reader.CSVReader
import Table.{TableInterface, TableModel}
import TableParser.{FileParser, ParseTableCells}
import Table.TableEntries.{Empty, TableEntry}
import ExpressionAST.EvaluationContext
import Evaluation.TableEvaluator
import Filters.Row

class BaseTable(parser: FileParser) extends TableInterface {

  private var rawModel: TableModel[TableEntry] = new TableModel(Map())
  private var evaluatedModel: TableModel[EvaluationResult[?]] = new TableModel(Map())

  override def initializeRows(parsedRows: TableModel[TableEntry]): Unit = {
    rawModel = parsedRows
  }

  override def getCell(pos: ParseTableCells): TableEntry = {
    rawModel.getCell(pos).getOrElse(
      throw new IllegalArgumentException(s"There is no cell found at position $pos")
    )
  }

  override def getRow(rowIndex: Int): Row = {
    new Row(rowIndex, evaluatedModel)
  }

  override def lastRow: Option[Int] = {
    rawModel.nonEmptyPositions.map(_.row).maxOption
  }

  override def lastColumn: Option[Int] = {
    rawModel.nonEmptyPositions.map(_.col).maxOption
  }

  override def nonEmptyPositions: Iterable[ParseTableCells] = {
    evaluatedModel.nonEmptyPositions
  }

  override def getRawEntires: Iterable[ParseTableCells] = rawModel.nonEmptyPositions

  override def storeEvaluatedResult(pos: ParseTableCells, result: EvaluationResult[?]): Unit = {
    evaluatedModel = evaluatedModel.updateCell(pos, result)
  }

  override def getEvaluatedResult(pos: ParseTableCells): Option[EvaluationResult[?]] = {
    evaluatedModel.getCell(pos)
  }

  override def getEvaluatedResultAsString(pos: ParseTableCells): String = {
    evaluatedModel.getCell(pos).map {
      case IntResult(value) => value.toString
      case FloatResult(value) => value.toString
      case EmptyResult       => ""
      case _                 => "ERROR" //it will never reach this case, because we exit when the evaluation fails
    }.getOrElse(" ")
  }

  override def parse(csvReader: CSVReader): Unit = {
    initializeRows(parser.parse(csvReader))
  }

  override def evaluateAllCells(context: EvaluationContext): Unit = {
    val evaluator = new TableEvaluator(this, context)
    evaluator.evaluateAllCellsAndStoreResults()
  }
}
