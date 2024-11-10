package ExpressionAST

import Evaluation.EvaluationTypes.{EvaluationError, FloatResult, IntResult}
import Evaluation.EvaluationResult

//a case class that performs the addition of two expressions
case class AddExpression[T](left: Expression[T], right: Expression[T]) extends BinaryOperationExpression[T](left, right) {

  override def operator(leftValue: EvaluationResult[T], rightValue: EvaluationResult[T]): EvaluationResult[T] = {
    (leftValue, rightValue) match {
      case (IntResult(l), IntResult(r)) =>
        IntResult(l + r).asInstanceOf[EvaluationResult[T]] // Add two integers
      case (FloatResult(l), FloatResult(r)) =>
        FloatResult(l + r).asInstanceOf[EvaluationResult[T]] // Add two floats
      case (IntResult(l), FloatResult(r)) =>
        FloatResult(l + r).asInstanceOf[EvaluationResult[T]] // Promote Int to Float and add
      case (FloatResult(l), IntResult(r)) =>
        FloatResult(l + r).asInstanceOf[EvaluationResult[T]] // Promote Int to Float and add
      case _ =>
        EvaluationError("Operation not supported for these types").asInstanceOf[EvaluationResult[T]]
    }
  }
}
