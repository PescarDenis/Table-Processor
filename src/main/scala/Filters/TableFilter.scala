package Filters
import Evaluation.EvaluationTypes.EvaluationResult
import Table.ParseTableCells
//base interface for filters
//use contravariance in order to work with different types of evaluation results
//int,floats etc.
trait TableFilter {
  def matches(row: Map[ParseTableCells, EvaluationResult[_]]): Boolean
  // row represents a map where each key is the cell location, and each value is an EvaluationResult of any type
}