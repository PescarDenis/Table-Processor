package Filters

import Evaluation.EvaluationResult
import Evaluation.EvaluationTypes.EmptyResult

case class EmptyCellFilter(column: String, isEmpty: Boolean) extends TableFilter {

  override def matches(row: Row): Boolean = {
    row.getCellByColumnName(column) match {
      case Some(EmptyResult) => isEmpty //if the cell contains an empty results is empty indeed
      case Some(_)           => !isEmpty //otherwise is not
      case None              => throw new FilterError(s"The given column $column does not exists in the table") //if we don't find the collumn that we try to filter
      //specify it to the user
    }
  }
}
