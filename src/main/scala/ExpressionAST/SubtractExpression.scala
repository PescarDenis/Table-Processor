package ExpressionAST

import Evaluation.EvaluationTypes.{EmptyResult, EvaluationError, FloatResult, IntResult}
import Evaluation.EvaluationResult

//a case class that performs the subtraction of two expressions
case class SubtractExpression[T](left: Expression[T], right: Expression[T]) extends BinaryOperationExpression[T](left, right) {

  override def operator(leftValue: EvaluationResult[T], rightValue: EvaluationResult[T]): EvaluationResult[T] = {
    (leftValue, rightValue) match {
      case (EmptyResult, _) | (_, EmptyResult) =>
        EvaluationError("Empty cell in arithmetic operation") // Error for empty cells
      case (EvaluationError(msg), _) =>
        EvaluationError(msg)
      case (_, EvaluationError(msg)) => // Used for getting the error messages when the cyclical dependencies are detected
        EvaluationError(msg)

      case (IntResult(l), IntResult(r)) =>
        IntResult(l - r) // Subtract two integers
      case (FloatResult(l), FloatResult(r)) =>
        FloatResult(l - r)// Subtract two floats
      case (IntResult(l), FloatResult(r)) =>
        FloatResult(l - r) // Promote Int to Float and subtract
      case (FloatResult(l), IntResult(r)) =>
        FloatResult(l - r) // Promote Int to Float and subtract
      case _ =>
        EvaluationError("Unsupported operation between these types")
    }
  }
}
