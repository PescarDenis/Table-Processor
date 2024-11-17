package Filters

import Evaluation.EvaluationResult
import TableParser.ParseTableCells
//base interface for filters

trait TableFilter {
  def matches(row: Row): Boolean
}
