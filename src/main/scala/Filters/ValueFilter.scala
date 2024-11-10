package Filters

import Evaluation.EvaluationTypes.{FloatResult, IntResult}
import Evaluation.EvaluationResult
import TableParser.ParseTableCells

// ValueFilter for filtering by column values with comparison operators
case class ValueFilter(column: String, operator: String, value: Double) extends TableFilter {

  override def matches(row: Map[ParseTableCells, EvaluationResult[?]]): Boolean = {
    row.find { case (cell, _) => ParseTableCells.getColName(cell.col) == column } match {
      case Some((_, IntResult(cellValue))) =>
        applyOperator(cellValue.toDouble, value, operator) //convert Int to Double

      case Some((_, FloatResult(cellValue))) =>
        applyOperator(cellValue, value, operator)

      case _ => false //unsupported or mismatched types
    }
  }
  //helper method to apply the specified comparison operator
  private def applyOperator[T](cellValue: T, targetValue: T, operator: String)(implicit ord: Ordering[T]): Boolean = {
    operator match {
      case "<"  => ord.lt(cellValue, targetValue)
      case ">"  => ord.gt(cellValue, targetValue)
      case "<=" => ord.lteq(cellValue, targetValue)
      case ">=" => ord.gteq(cellValue, targetValue)
      case "==" => ord.equiv(cellValue, targetValue)
      case "!=" => !ord.equiv(cellValue, targetValue)
      case _ => false
    }
  }
}
