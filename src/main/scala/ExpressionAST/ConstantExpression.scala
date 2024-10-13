package ExpressionAST

import Evaluation.EvaluationTypes.EvaluationResult
import Table.ParseTableCells

// Represents a constant numerical value in the formula
case class ConstantExpression[T](value: EvaluationResult[T]) extends Expression[T] {
  override def evaluate(context: EvaluationContext, visited: Set[ParseTableCells]): EvaluationResult[T] = value
  // Since the ConstantExpression doesn't reference any other cells, just return the value
}
