package Filters

import Evaluation.EvaluationTypes.{FloatResult, IntResult}
import Evaluation.EvaluationResult

case class ValueFilter(column: String, operator: String, value: Double) extends TableFilter {

  override def matches(row: Row): Boolean = {
    row.getCellByColumnName(column) match {
      case Some(IntResult(cellValue))  => applyOperator(cellValue.toDouble, value)
      case Some(FloatResult(cellValue)) => applyOperator(cellValue, value)
      case _ => false
    }
  }

  private def applyOperator(cellValue: Double, targetValue: Double): Boolean = {
    operator match {
      case "<"  => cellValue < targetValue
      case ">"  => cellValue > targetValue
      case "<=" => cellValue <= targetValue
      case ">=" => cellValue >= targetValue
      case "==" => cellValue == targetValue
      case "!=" => cellValue != targetValue
      case _    => false
    }
  }
}
