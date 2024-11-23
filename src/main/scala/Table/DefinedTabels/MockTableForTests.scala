package Table.DefinedTabels

import Evaluation.EvaluationTypes.{EmptyResult, FloatResult, IntResult}
import Evaluation.EvaluationResult
import TableParser.{FileParser, ParseTableCells}
import ExpressionParser.ParserLogic.*
import ExpressionParser.ParserLogic.ExpressionBuilder.ExpressionBuilder
import ExpressionParser.ParsingServices.DefaultExpressionParser
import Table.TableModel
import Filters.Row

//this is a mock table used for testing
//it just overwrites some of the Base table methods used for gettting or setting the data
class MockTableForTests(
                         initialData: Map[ParseTableCells, EvaluationResult[?]]
                       ) extends BaseTable(new FileParser(new DefaultExpressionParser(new ExpressionBuilder))) {

  // Use TableModel to encapsulate evaluated results
  protected var evaluatedResults: TableModel[EvaluationResult[?]] = new TableModel(initialData)

  override def getEvaluatedResult(cell: ParseTableCells): Option[EvaluationResult[?]] = {
    evaluatedResults.getCell(cell)
  }

  override def lastRow: Option[Int] = {
    evaluatedResults.nonEmptyPositions.map(_.row).maxOption
  }

  override def lastColumn: Option[Int] = {
    evaluatedResults.nonEmptyPositions.map(_.col).maxOption
  }

  // Implement getRow to return a Row object
  override def getRow(rowIndex: Int): Row = {
    new Row(rowIndex, evaluatedResults)
  }

  override def nonEmptyPositions: Iterable[ParseTableCells] = {
    evaluatedResults.nonEmptyPositions
  }

  override def getEvaluatedResultAsString(pos: ParseTableCells): String = {
    evaluatedResults.getCell(pos).map {
      case IntResult(value)  => value.toString
      case FloatResult(value) => value.toString
      case EmptyResult        => ""
      case _                 => "Error"
    }.getOrElse("   ")
  }
}
