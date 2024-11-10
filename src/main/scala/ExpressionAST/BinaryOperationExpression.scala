package ExpressionAST

import Evaluation.EvaluationResult
import TableParser.ParseTableCells

// Abstract class for binary operations like +, -, *, /
abstract class BinaryOperationExpression[T](left: Expression[T], right: Expression[T]) extends Expression[T] {

  def operator(leftValue: EvaluationResult[T], rightValue: EvaluationResult[T]): EvaluationResult[T]

  // Ensure the result is cast to EvaluationResult[T]
  override def evaluate(context: EvaluationContext, visited: Set[ParseTableCells]): EvaluationResult[T] = {
    val leftValue = left.evaluate(context, visited)  // Evaluate the left sub-expression
    val rightValue = right.evaluate(context, visited) // Evaluate the right sub-expression
    operator(leftValue, rightValue)
  }
}
