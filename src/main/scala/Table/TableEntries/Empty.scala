package Table.TableEntries

import Evaluation.EvaluationResult
import ExpressionAST.EvaluationContext
import Evaluation.EvaluationTypes.*
import TableParser.ParseTableCells

//empty cell entry 
case class Empty(row: Int, col: Int) extends TableEntry {
  override def get: String = ""
  override def set(value: String): Unit = {} // No-op
  override def isEmpty: Boolean = true

  override def evaluate(context: EvaluationContext, visited: Set[ParseTableCells]): EvaluationResult[Nothing] = EmptyResult
}
