package ExpressionAST

import Evaluation.EvaluationResult
import TableParser.ParseTableCells

case class ConstantExpression[T](value: EvaluationResult[T]) extends Expression[T] {
  override def evaluate(context: EvaluationContext, visited: Set[ParseTableCells]): EvaluationResult[T] = value
}
