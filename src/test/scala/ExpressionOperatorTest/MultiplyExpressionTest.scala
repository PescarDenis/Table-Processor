package ExpressionOperatorTest

import org.scalatest.funsuite.AnyFunSuite
import ExpressionAST._
import Evaluation.EvaluationTypes.{EmptyResult, EvaluationError, FloatResult, IntResult}
import Evaluation.EvaluationResult

class MultiplyExpressionTest extends AnyFunSuite {

  test("Multiply two integers") {
    val leftValue = IntResult(5)
    val rightValue = IntResult(10)

    val multiplyExpr = MultiplyExpression[Int](null, null)
    val result = multiplyExpr.operator(leftValue, rightValue)

    assert(result == IntResult(50))
  }

  test("Multiply two floats") {
    val leftValue = FloatResult(2.5)
    val rightValue = FloatResult(3.5)

    val multiplyExpr = MultiplyExpression[Double](null, null)
    val result = multiplyExpr.operator(leftValue, rightValue)

    assert(result == FloatResult(8.75))
  }

  test("Multiply an integer and a float") {
    val leftValue = IntResult(5)
    val rightValue = FloatResult(2.5)

    val multiplyExpr = MultiplyExpression[Double](null, null)
    val result = multiplyExpr.operator(leftValue.asInstanceOf[EvaluationResult[Double]], rightValue)

    assert(result == FloatResult(12.5))
  }

  test("Multiply a float and an integer") {
    val leftValue = FloatResult(3.5)
    val rightValue = IntResult(2)

    val multiplyExpr = MultiplyExpression[Double](null, null)
    val result = multiplyExpr.operator(leftValue, rightValue.asInstanceOf[EvaluationResult[Double]])

    assert(result == FloatResult(7.0))
  }

  test("Handle empty result on the left") {
    val leftValue = EmptyResult
    val rightValue = IntResult(5)

    val multiplyExpr = MultiplyExpression[Int](null, null)
    val result = multiplyExpr.operator(leftValue, rightValue)

    assert(result == EvaluationError("Empty cell in arithmetic operation"))
  }

  test("Handle empty result on the right") {
    val leftValue = IntResult(10)
    val rightValue = EmptyResult

    val multiplyExpr = MultiplyExpression[Int](null, null)
    val result = multiplyExpr.operator(leftValue, rightValue)

    assert(result == EvaluationError("Empty cell in arithmetic operation"))
  }

  test("Handle evaluation error on the left") {
    val leftValue = EvaluationError("Error in left operand")
    val rightValue = IntResult(5)

    val multiplyExpr = MultiplyExpression[Int](null, null)
    val result = multiplyExpr.operator(leftValue, rightValue)

    assert(result == EvaluationError("Error in left operand"))
  }

  test("Handle evaluation error on the right") {
    val leftValue = IntResult(10)
    val rightValue = EvaluationError("Error in right operand")

    val multiplyExpr = MultiplyExpression[Int](null, null)
    val result = multiplyExpr.operator(leftValue, rightValue)

    assert(result == EvaluationError("Error in right operand"))
  }
}
