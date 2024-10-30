package Table.DefinedTabels

import Evaluation.EvaluationTypes._
import Table.ParseTableCells
import Table.DefinedTabels.BaseTable

class MockTableForTests(initialData: Map[ParseTableCells, EvaluationResult[_]]) extends BaseTable {
  // Initialize evaluatedResults with data passed from the test
  protected var evaluatedResults: Map[ParseTableCells, EvaluationResult[_]] = initialData

  // Override getEvaluatedResult to fetch directly from the evaluatedResults map
  override def getEvaluatedResult(cell: ParseTableCells): Option[EvaluationResult[_]] = {
    evaluatedResults.get(cell)
  }

  // Override lastRow to reflect the maximum row populated
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
      case EmptyResult => " "
      case _ => "Error" // or other placeholder for unexpected cases
    }.getOrElse("   ")
  }
}
