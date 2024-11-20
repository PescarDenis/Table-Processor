package ExpressionAST

import Evaluation.EvaluationTypes.{EmptyResult, EvaluationError, FloatResult, IntResult}
import Evaluation.EvaluationResult

//a case class that performs the multiplication of two expressions
case class MultiplyExpression[T](left: Expression[T], right: Expression[T]) extends BinaryOperationExpression[T](left, right) {

  override def operator(leftValue: EvaluationResult[T], rightValue: EvaluationResult[T]): EvaluationResult[T] = {
    (leftValue, rightValue) match {
      case (EmptyResult, _) | (_, EmptyResult) =>
        EvaluationError("Empty cell in arithmetic operation") // Error for empty cells
      case (EvaluationError(msg), _) =>
        EvaluationError(msg)
      case (_, EvaluationError(msg)) => // Used for getting the error messages when the cyclical dependencies are detected
        EvaluationError(msg)


      case (IntResult(l), IntResult(r)) =>
        IntResult(l * r) // Multiply two integers
      case (FloatResult(l), FloatResult(r)) =>
        FloatResult(l * r)// Multiply two floats
      case (IntResult(l), FloatResult(r)) =>
        FloatResult(l * r) // Promote Int to Float and multiply
      case (FloatResult(l), IntResult(r)) =>
        FloatResult(l * r).asInstanceOf[EvaluationResult[T]] // Promote Int to Float and multiply
      case _ =>
        EvaluationError("Unsupported operation between these types")
    }
  }
}
