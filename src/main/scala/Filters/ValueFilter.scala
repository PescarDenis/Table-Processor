package Filters

import Evaluation.EvaluationTypes.{FloatResult, IntResult}
import Evaluation.EvaluationResult

case class ValueFilter(column: String, operator: String, value: Double) extends TableFilter {

  override def matches(row: Row): Boolean = {
    row.getCellByColumnName(column) match {
      case Some(IntResult(cellValue))  => applyOperator(cellValue.toDouble, value)
      case Some(FloatResult(cellValue)) => applyOperator(cellValue, value)
      case Some(_) => false //if you apply a value filter to an empty results cell just returns false
      case None => throw FilterError(s"The given column $column does not exists in the table") //the same goes for the value filter if we don't find the collumn
    }
  }
  //helper method to apply the given operators
  private def applyOperator(cellValue: Double, targetValue: Double): Boolean = {
    operator match {
      case "<"  => cellValue < targetValue
      case ">"  => cellValue > targetValue
      case "<=" => cellValue <= targetValue
      case ">=" => cellValue >= targetValue
      case "==" => cellValue == targetValue
      case "!=" => cellValue != targetValue
      case _    => throw FilterError(s"Unsupported filtering operator: $operator") // If the operator is not defined
    }
  }
}
