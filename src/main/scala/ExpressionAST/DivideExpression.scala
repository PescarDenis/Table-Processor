package ExpressionAST

import Evaluation.EvaluationTypes.{EvaluationError, EvaluationResult, FloatResult, IntResult}

//a case class that performs the division of two expressions
case class DivideExpression[T](left: Expression[T], right: Expression[T]) extends BinaryOperationExpression[T](left, right) {

  override def operator(leftValue: EvaluationResult[T], rightValue: EvaluationResult[T]): EvaluationResult[T] = {
    (leftValue, rightValue) match {
      case (IntResult(l), IntResult(r)) if r != 0 =>
        if (l % r == 0) IntResult(l / r).asInstanceOf[EvaluationResult[T]] //in case we get a reminder = 0 return the result as an int
        else FloatResult(l.toDouble / r.toDouble).asInstanceOf[EvaluationResult[T]] //otherwise as a float

      case (FloatResult(l), FloatResult(r)) if r != 0.0 =>
        FloatResult(l / r).asInstanceOf[EvaluationResult[T]]

      case (IntResult(l), FloatResult(r)) if r != 0.0 =>
        FloatResult(l.toDouble / r).asInstanceOf[EvaluationResult[T]]

      case (FloatResult(l), IntResult(r)) if r != 0 =>
        FloatResult(l / r.toDouble).asInstanceOf[EvaluationResult[T]]

      case (IntResult(_), IntResult(0)) | (FloatResult(_), FloatResult(0.0)) =>
        throw new ArithmeticException("Division by zero") //division by 0 should not be supported

      case _ =>
        EvaluationError("Operation not supported for these types").asInstanceOf[EvaluationResult[T]]
    }
  }
}
