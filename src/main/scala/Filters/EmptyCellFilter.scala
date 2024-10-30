package Filters

import Table.ParseTableCells
import Evaluation.EvaluationTypes.{EmptyResult, EvaluationResult}

// Single class to filter empty or non-empty cells
case class EmptyCellFilter(column: String, isEmpty: Boolean) extends TableFilter {

  override def matches(row: Map[ParseTableCells, EvaluationResult[_]]): Boolean = {
    row.find { case (cell, _) => ParseTableCells.getColName(cell.col) == column } match {
      case Some((_, EmptyResult)) => isEmpty
      case Some((_, _)) => !isEmpty
      case None => isEmpty // If cell is missing, treat it as empty if isEmpty is true
    }
  }
}

