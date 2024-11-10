package Filters

import Evaluation.EvaluationResult
import TableParser.ParseTableCells

//base interface for filters
trait TableFilter {
  def matches(row: Map[ParseTableCells, EvaluationResult[?]]): Boolean
  // row represents a map where each key is the cell location, and each value is an EvaluationResult of any type
}