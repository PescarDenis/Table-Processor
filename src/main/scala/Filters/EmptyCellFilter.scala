package Filters

import Table.ParseTableCells
import Evaluation.EvaluationTypes.{EvaluationResult, EmptyResult}

// Single class to filter empty or non-empty cells
case class EmptyCellFilter(column: Table.ParseTableCells, isEmpty: Boolean) extends TableFilter[Any] {
  override def matches(row: Map[Table.ParseTableCells, EvaluationResult[_]]): Boolean = {
    row.get(column) match {
      case Some(EmptyResult) => isEmpty // True if looking for empty cells
      case _ => !isEmpty // True if looking for non-empty cells
    }
  }
}
