package ExpressionOperatorTest

import org.scalatest.funsuite.AnyFunSuite
import ExpressionAST._
import Evaluation.EvaluationTypes.{EmptyResult, EvaluationError, FloatResult, IntResult}
import Evaluation.EvaluationResult

//Test the operator for Add expression
class AddExpressionTest extends AnyFunSuite {

  test("Add two integers") {
    val leftValue = IntResult(5)
    val rightValue = IntResult(10)

    val addExpr = AddExpression[Int](null, null)
    val result = addExpr.operator(leftValue, rightValue)

    assert(result == IntResult(15))
  }

  test("Add two floats") {
    val leftValue = FloatResult(2.5)
    val rightValue = FloatResult(3.5)

    val addExpr = AddExpression[Double](null, null)
    val result = addExpr.operator(leftValue, rightValue)

    assert(result == FloatResult(6.0))
  }

  test("Add an integer and a float") {
    val leftValue = IntResult(5)
    val rightValue = FloatResult(2.5)

    val addExpr = AddExpression[Double](null, null)
    val result = addExpr.operator(leftValue.asInstanceOf[EvaluationResult[Double]], rightValue)

    assert(result == FloatResult(7.5))
  }

  test("Add a float and an integer") {
    val leftValue = FloatResult(3.5)
    val rightValue = IntResult(2)

    val addExpr = AddExpression[Double](null, null)
    val result = addExpr.operator(leftValue, rightValue.asInstanceOf[EvaluationResult[Double]])

    assert(result == FloatResult(5.5))
  }

  test("Handle empty result on the left") {
    val leftValue = EmptyResult
    val rightValue = IntResult(10)

    val addExpr = AddExpression[Int](null, null)
    val result = addExpr.operator(leftValue, rightValue)

    assert(result == EvaluationError("Empty cell in arithmetic operation"))
  }

  test("Handle evaluation error on the right") {
    val leftValue = FloatResult(10.0)
    val rightValue = EvaluationError("Error in right operand")

    val addExpr = AddExpression[Double](null, null)
    val result = addExpr.operator(leftValue, rightValue.asInstanceOf[EvaluationResult[Double]])

    assert(result == EvaluationError("Error in right operand"))
  }
  test("Handle evaluation error on the left") {
    val leftValue = EvaluationError("Error in left operand")
    val rightValue = IntResult(1231)

    val addExpr = AddExpression[Int](null, null)
    val result = addExpr.operator(leftValue.asInstanceOf[EvaluationResult[Int]], rightValue)

    assert(result ==  EvaluationError("Error in left operand"))
  }

  test("Handle unsupported types") {
    val leftValue = EmptyResult
    val rightValue = FloatResult(10.0)

    val addExpr = AddExpression[Any](null, null)
    val result = addExpr.operator(leftValue.asInstanceOf[EvaluationResult[Any]], rightValue.asInstanceOf[EvaluationResult[Any]])

    assert(result.isInstanceOf[EvaluationError])
    assert(result == EvaluationError("Empty cell in arithmetic operation"))
  }
}
