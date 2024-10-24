package Filters

import Table.ParseTableCells
import Evaluation.EvaluationTypes.{EvaluationResult,IntResult,FloatResult}
// Abstract class for value filtering
case class ValueFilter[T](column: Table.ParseTableCells, operator: String, value: T)(implicit ordering: Ordering[T]) extends TableFilter[T] {
  override def matches(row: Map[ParseTableCells, EvaluationResult[_]]): Boolean = {
    row.get(column) match {
      case Some(IntResult(v)) if value.isInstanceOf[Int] =>
        compareValues(v.asInstanceOf[T])
      case Some(FloatResult(v)) if value.isInstanceOf[Double] =>
        compareValues(v.asInstanceOf[T])
      case _ => false
    }
  }

  private def compareValues(cellValue: T): Boolean = {
    operator match {
      case "<"  => ordering.lt(cellValue, value)
      case ">"  => ordering.gt(cellValue, value)
      case "<=" => ordering.lteq(cellValue, value)
      case ">=" => ordering.gteq(cellValue, value)
      case "==" => ordering.equiv(cellValue, value)
      case "!=" => !ordering.equiv(cellValue, value)
      case _ => false
    }
  }
}

