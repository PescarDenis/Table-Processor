package ExpressionOperatorTest

import org.scalatest.funsuite.AnyFunSuite
import ExpressionAST._
import Evaluation.EvaluationTypes.{EmptyResult, EvaluationError, FloatResult, IntResult}
import Evaluation.EvaluationResult

class DivideExpressionTest extends AnyFunSuite {

  test("Divide two integers with no remainder") {
    val leftValue = IntResult(10)
    val rightValue = IntResult(2)

    val divideExpr = DivideExpression[Int](null, null)
    val result = divideExpr.operator(leftValue, rightValue)

    assert(result == IntResult(5))
  }

  test("Divide two integers with a remainder") {
    val leftValue = IntResult(10)
    val rightValue = IntResult(3)

    val divideExpr = DivideExpression[Double](null, null)
    val result = divideExpr.operator(leftValue.asInstanceOf[EvaluationResult[Double]], rightValue.asInstanceOf[EvaluationResult[Double]])

    assert(result == FloatResult(10.0 / 3.0))
  }

  test("Divide two floats") {
    val leftValue = FloatResult(7.5)
    val rightValue = FloatResult(2.5)

    val divideExpr = DivideExpression[Double](null, null)
    val result = divideExpr.operator(leftValue, rightValue)

    assert(result == FloatResult(3.0))
  }

  test("Divide an integer by a float") {
    val leftValue = IntResult(10)
    val rightValue = FloatResult(2.5)

    val divideExpr = DivideExpression[Double](null, null)
    val result = divideExpr.operator(leftValue.asInstanceOf[EvaluationResult[Double]], rightValue)

    assert(result == FloatResult(4.0))
  }

  test("Divide a float by an integer") {
    val leftValue = FloatResult(10.0)
    val rightValue = IntResult(4)

    val divideExpr = DivideExpression[Double](null, null)
    val result = divideExpr.operator(leftValue, rightValue.asInstanceOf[EvaluationResult[Double]])

    assert(result == FloatResult(2.5))
  }

  test("Handle division by zero (integer)") {
    val leftValue = IntResult(10)
    val rightValue = IntResult(0)

    val divideExpr = DivideExpression[Int](null, null)
    val result = divideExpr.operator(leftValue, rightValue)

    assert(result == EvaluationError("Division by zero is not supported"))
  }

  test("Handle division by zero (float)") {
    val leftValue = FloatResult(10.0)
    val rightValue = FloatResult(0.0)

    val divideExpr = DivideExpression[Double](null, null)
    val result = divideExpr.operator(leftValue, rightValue)

    assert(result == EvaluationError("Division by zero is not supported"))
  }

  test("Handle empty result on the left") {
    val leftValue = EmptyResult
    val rightValue = IntResult(5)

    val divideExpr = DivideExpression[Int](null, null)
    val result = divideExpr.operator(leftValue, rightValue)

    assert(result == EvaluationError("Empty cell in arithmetic operation"))
  }

  test("Handle empty result on the right") {
    val leftValue = IntResult(10)
    val rightValue = EmptyResult

    val divideExpr = DivideExpression[Int](null, null)
    val result = divideExpr.operator(leftValue, rightValue)

    assert(result == EvaluationError("Empty cell in arithmetic operation"))
  }

  test("Handle evaluation error on the left") {
    val leftValue = EvaluationError("Error in left operand")
    val rightValue = IntResult(5)

    val divideExpr = DivideExpression[Int](null, null)
    val result = divideExpr.operator(leftValue, rightValue)

    assert(result == EvaluationError("Error in left operand"))
  }

  test("Handle evaluation error on the right") {
    val leftValue = IntResult(10)
    val rightValue = EvaluationError("Error in right operand")

    val divideExpr = DivideExpression[Int](null, null)
    val result = divideExpr.operator(leftValue, rightValue)

    assert(result == EvaluationError("Error in right operand"))
  }
}

