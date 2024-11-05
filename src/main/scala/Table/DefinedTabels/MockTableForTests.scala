package Table.DefinedTabels

import Evaluation.EvaluationTypes._
import Table.ParseTableCells

//just a moktable for testing purposes
class MockTableForTests(initialData: Map[ParseTableCells, EvaluationResult[_]]) extends BaseTable {
  protected var evaluatedResults: Map[ParseTableCells, EvaluationResult[_]] = initialData

  override def getEvaluatedResult(cell: ParseTableCells): Option[EvaluationResult[_]] = {
    evaluatedResults.get(cell)
  }

  override def lastRow: Option[Int] = {
    if (evaluatedResults.isEmpty) None else evaluatedResults.keys.map(_.row).maxOption
  }

  override def lastColumn: Option[Int] = {
    if (evaluatedResults.isEmpty) None else evaluatedResults.keys.map(_.col).maxOption
  }

  override def getRow(rowIndex: Int): Map[ParseTableCells, EvaluationResult[_]] = {
    evaluatedResults.filter { case (cell, _) => cell.row == rowIndex }
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
