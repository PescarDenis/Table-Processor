package Filters
import Evaluation.EvaluationTypes.EvaluationResult
import Table.ParseTableCells
//base interface for filters
trait TableFilter {
  def matches(row: Map[ParseTableCells, EvaluationResult[_]]): Boolean
  // row represents a map where each key is the cell location, and each value is an EvaluationResult of any type
}