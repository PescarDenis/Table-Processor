package ExpressionOperatorTest

import org.scalatest.funsuite.AnyFunSuite
import ExpressionAST._
import Evaluation.EvaluationTypes.{EmptyResult, EvaluationError, FloatResult, IntResult}
import Evaluation.EvaluationResult

class SubtractExpressionTest extends AnyFunSuite {

  test("Subtract two integers") {
    val leftValue = IntResult(10)
    val rightValue = IntResult(5)

    val subtractExpr = SubtractExpression[Int](null, null) // Testing only operator
    val result = subtractExpr.operator(leftValue, rightValue)

    assert(result == IntResult(5))
  }

  test("Subtract two floats") {
    val leftValue = FloatResult(5.5)
    val rightValue = FloatResult(2.2)

    val subtractExpr = SubtractExpression[Double](null, null)
    val result = subtractExpr.operator(leftValue, rightValue)

    assert(result == FloatResult(3.3))
  }

  test("Subtract an integer from a float") {
    val leftValue = FloatResult(10.5)
    val rightValue = IntResult(5)

    val subtractExpr = SubtractExpression[Double](null, null)
    val result = subtractExpr.operator(leftValue, rightValue.asInstanceOf[EvaluationResult[Double]])

    assert(result == FloatResult(5.5))
  }

  test("Subtract a float from an integer") {
    val leftValue = IntResult(10)
    val rightValue = FloatResult(5.5)

    val subtractExpr = SubtractExpression[Double](null, null)
    val result = subtractExpr.operator(leftValue.asInstanceOf[EvaluationResult[Double]], rightValue)

    assert(result == FloatResult(4.5))
  }

  test("Handle empty result on the left") {
    val leftValue = EmptyResult
    val rightValue = IntResult(5)

    val subtractExpr = SubtractExpression[Int](null, null)
    val result = subtractExpr.operator(leftValue, rightValue)

    assert(result == EvaluationError("Empty cell in arithmetic operation"))
  }

  test("Handle empty result on the right") {
    val leftValue = IntResult(10)
    val rightValue = EmptyResult

    val subtractExpr = SubtractExpression[Int](null, null)
    val result = subtractExpr.operator(leftValue, rightValue)

    assert(result == EvaluationError("Empty cell in arithmetic operation"))
  }

  test("Handle evaluation error on the left") {
    val leftValue = EvaluationError("Error in left operand")
    val rightValue = IntResult(5)

    val subtractExpr = SubtractExpression[Int](null, null)
    val result = subtractExpr.operator(leftValue, rightValue)

    assert(result == EvaluationError("Error in left operand"))
  }

  test("Handle evaluation error on the right") {
    val leftValue = IntResult(10)
    val rightValue = EvaluationError("Error in right operand")

    val subtractExpr = SubtractExpression[Int](null, null)
    val result = subtractExpr.operator(leftValue, rightValue)

    assert(result == EvaluationError("Error in right operand"))
  }
}

