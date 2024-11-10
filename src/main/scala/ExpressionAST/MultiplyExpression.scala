package ExpressionAST

import Evaluation.EvaluationTypes.{EvaluationError, FloatResult, IntResult}
import Evaluation.EvaluationResult

//a case class that performs the multiplication of two expressions
case class MultiplyExpression[T](left: Expression[T], right: Expression[T]) extends BinaryOperationExpression[T](left, right) {

  override def operator(leftValue: EvaluationResult[T], rightValue: EvaluationResult[T]): EvaluationResult[T] = {
    (leftValue, rightValue) match {
      case (IntResult(l), IntResult(r)) =>
        IntResult(l * r).asInstanceOf[EvaluationResult[T]] // Multiply two integers
      case (FloatResult(l), FloatResult(r)) =>
        FloatResult(l * r).asInstanceOf[EvaluationResult[T]] // Multiply two floats
      case (IntResult(l), FloatResult(r)) =>
        FloatResult(l * r).asInstanceOf[EvaluationResult[T]] // Promote Int to Float and multiply
      case (FloatResult(l), IntResult(r)) =>
        FloatResult(l * r).asInstanceOf[EvaluationResult[T]] // Promote Int to Float and multiply
      case _ =>
        EvaluationError("Operation not supported for these types").asInstanceOf[EvaluationResult[T]]
    }
  }
}
