package Filters

import Evaluation.EvaluationResult
import TableParser.ParseTableCells

///This filter allows combining multiple filters. It only passes rows that match all the filters in the list (logical AND).
case class ChainedFilter(filters: List[TableFilter]) extends TableFilter {

  override def matches(row: Map[ParseTableCells, EvaluationResult[?]]): Boolean = {
    filters.forall(_.matches(row))
  }
}