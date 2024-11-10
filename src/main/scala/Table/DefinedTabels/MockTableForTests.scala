package Table.DefinedTabels

import Evaluation.EvaluationTypes.{EmptyResult, FloatResult, IntResult}
import Evaluation.EvaluationResult
import TableParser.{FileParser, ParseTableCells}
import ExpressionParser.ParserLogic.*
import ExpressionParser.ParsingServices.DefaultExpressionParser

class MockTableForTests(
                         initialData: Map[ParseTableCells, EvaluationResult[?]]
                       ) extends BaseTable(new FileParser(new DefaultExpressionParser(new ExpressionBuilder))) {


  protected var evaluatedResults: Map[ParseTableCells, EvaluationResult[?]] = initialData

  override def getEvaluatedResult(cell: ParseTableCells): Option[EvaluationResult[?]] = {
    evaluatedResults.get(cell)
  }

  override def lastRow: Option[Int] = {
    if (evaluatedResults.isEmpty) None else evaluatedResults.keys.map(_.row).maxOption
  }

  override def lastColumn: Option[Int] = {
    if (evaluatedResults.isEmpty) None else evaluatedResults.keys.map(_.col).maxOption
  }

  override def getRow(rowIndex: Int): Map[ParseTableCells, EvaluationResult[?]] = {
    evaluatedResults.filter { case (cell, _) => cell.row == rowIndex }
  }

  override def nonEmptyPositions: Iterable[ParseTableCells] = {
    evaluatedResults.collect {
      case (pos, result) if !result.isInstanceOf[EmptyResult.type] => pos
    }.toList
  }

  override def getEvaluatedResultAsString(pos: ParseTableCells): String = {
    evaluatedResults.get(pos).map {
      case IntResult(value) => value.toString
      case FloatResult(value) => value.toString
      case EmptyResult => ""
      case _ => "Error"
    }.getOrElse("   ")
  }
}
