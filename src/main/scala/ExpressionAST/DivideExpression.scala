package ExpressionAST

import Evaluation.EvaluationTypes.{EmptyResult, EvaluationError, FloatResult, IntResult}
import Evaluation.EvaluationResult

//a case class that performs the division of two expressions
case class DivideExpression[T](left: Expression[T], right: Expression[T]) extends BinaryOperationExpression[T](left, right) {

  override def operator(leftValue: EvaluationResult[T], rightValue: EvaluationResult[T]): EvaluationResult[T] = {
    (leftValue, rightValue) match {
      case (EmptyResult, _) | (_, EmptyResult) =>
        EvaluationError("Empty cell in arithmetic operation") // Error for empty cells
      case (EvaluationError(msg), _) =>
        EvaluationError(msg)
      case (_, EvaluationError(msg)) =>
        EvaluationError(msg)


      case (IntResult(l), IntResult(r)) if r != 0 =>
        if (l % r == 0) IntResult(l / r)//in case we get a reminder = 0 return the result as an int
        else FloatResult(l.toDouble / r.toDouble).asInstanceOf[EvaluationResult[T]] //otherwise as a float

      case (FloatResult(l), FloatResult(r)) if r != 0.0 =>
        FloatResult(l / r)

      case (IntResult(l), FloatResult(r)) if r != 0.0 =>
        FloatResult(l.toDouble / r)

      case (FloatResult(l), IntResult(r)) if r != 0 =>
        FloatResult(l / r.toDouble)

      case (IntResult(_), IntResult(0)) | (FloatResult(_), FloatResult(0.0)) =>
        throw new ArithmeticException("Division by zero") //division by 0 should not be supported

      case _ =>
        EvaluationError("Unsupported operation between these types")
    }
  }
}
