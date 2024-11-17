package Filters

import Evaluation.EvaluationResult
import Evaluation.EvaluationTypes.EmptyResult

case class EmptyCellFilter(column: String, isEmpty: Boolean) extends TableFilter {

  override def matches(row: Row): Boolean = {
    row.getCellByColumnName(column) match {
      case Some(EmptyResult) => isEmpty
      case Some(_)           => !isEmpty
      case None              => isEmpty // If cell is missing, treat it as empty if isEmpty is true
    }
  }
}
