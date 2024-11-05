package ExpressionAST

import Evaluation.EvaluationTypes.{EvaluationResult, FloatResult, IntResult}
import Evaluation.EvaluationError

//a case class that performs the subtraction of two expressions
case class SubtractExpression[T](left: Expression[T], right: Expression[T]) extends BinaryOperationExpression[T](left, right) {

  override def operator(leftValue: EvaluationResult[T], rightValue: EvaluationResult[T]): EvaluationResult[T] = {
    (leftValue, rightValue) match {
      case (IntResult(l), IntResult(r)) =>
        IntResult(l - r).asInstanceOf[EvaluationResult[T]] // Subtract two integers
      case (FloatResult(l), FloatResult(r)) =>
        FloatResult(l - r).asInstanceOf[EvaluationResult[T]] // Subtract two floats
      case (IntResult(l), FloatResult(r)) =>
        FloatResult(l - r).asInstanceOf[EvaluationResult[T]] // Promote Int to Float and subtract
      case (FloatResult(l), IntResult(r)) =>
        FloatResult(l - r).asInstanceOf[EvaluationResult[T]] // Promote Int to Float and subtract
      case _ =>
        EvaluationError("Operation not supported for these types").asInstanceOf[EvaluationResult[T]]
    }
  }
}
