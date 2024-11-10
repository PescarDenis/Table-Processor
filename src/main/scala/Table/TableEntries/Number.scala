package Table.TableEntries

import ExpressionAST.EvaluationContext
import Evaluation.EvaluationTypes.*
import Evaluation.EvaluationResult
import TableParser.ParseTableCells

// Represents a table entry containing a number
case class Number(row: Int, col: Int) extends TableEntry {

  private var value: Option[Int] = None

  override def get: String = value.map(_.toString).getOrElse("")

  override def set(newValue: String): Unit = {
    val intValue = newValue.trim.toInt
    if (intValue < 0) {
      throw new IllegalArgumentException(s"Invalid value for Number entry at ($row, $col): $intValue (must be a positive integer)")
    }
    value = Some(intValue)
  }

  override def isEmpty: Boolean = value.isEmpty

  override def evaluate(context: EvaluationContext, visited: Set[ParseTableCells]): EvaluationResult[Int] =
    IntResult(value.getOrElse(throw new IllegalStateException(s"No value set for Number at ($row, $col)")))
}
