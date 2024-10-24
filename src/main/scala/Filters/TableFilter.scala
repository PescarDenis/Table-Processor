package Filters
import Evaluation.EvaluationTypes.EvaluationResult

//base interface for filters
//use contravariance in order to work with different types of evaluation results
//int,floats etc.
trait TableFilter[-T] {
  def matches(row: Map[Table.ParseTableCells, EvaluationResult[?]]): Boolean
}