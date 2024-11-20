package Filters


import TableParser.ParseTableCells
import Evaluation.EvaluationResult
import Table.TableModel

// Pass this for the match method, it provides a better abstraction 
class Row(rowIndex: Int, model: TableModel[EvaluationResult[?]]) {

  // Get all cells in this row as a Map
  private def getCells: Map[ParseTableCells, EvaluationResult[?]] = {
    model.toMap.filter { case (pos, _) => pos.row == rowIndex }
  }

  // Get a specific cell's result by column name
  def getCellByColumnName(columnName: String): Option[EvaluationResult[?]] = {
    getCells.find { case (pos, _) => ParseTableCells.getColName(pos.col) == columnName }.map(_._2)
  }

}

