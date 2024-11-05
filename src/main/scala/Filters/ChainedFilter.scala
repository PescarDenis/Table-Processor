package Filters
import Evaluation.EvaluationTypes.EvaluationResult
import Table.ParseTableCells

///This filter allows combining multiple filters. It only passes rows that match all the filters in the list (logical AND).
case class ChainedFilter(filters: List[TableFilter]) extends TableFilter {

  override def matches(row: Map[ParseTableCells, EvaluationResult[_]]): Boolean = {
    filters.forall(_.matches(row))
  }
}