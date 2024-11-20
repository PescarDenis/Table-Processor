package ExpressionAST

import Evaluation.EvaluationResult
import TableParser.ParseTableCells


// Knows that we have a constant expression like a Number or Empty cell and returns its Evaluation result 
case class ConstantExpression[T](value: EvaluationResult[T]) extends Expression[T] {
  override def evaluate(context: EvaluationContext, visited: Set[ParseTableCells]): EvaluationResult[T] = value
}
