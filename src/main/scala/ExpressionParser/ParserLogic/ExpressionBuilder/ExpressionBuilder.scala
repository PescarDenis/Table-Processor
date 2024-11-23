package ExpressionParser.ParserLogic.ExpressionBuilder

import Evaluation.EvaluationResult
import Evaluation.EvaluationTypes.{FloatResult, IntResult}
import ExpressionAST.*
import TableParser.ParseTableCells

// ExpressionBuilder class -> used to construct different types of expressions
class ExpressionBuilder[T] extends ExpressionBuilderInterface[T] {

  // Builds a constant expression based on the number type (int or float)
  override def buildConstant(value: String): ConstantExpression[T] = {
    if (value.contains('.')) {
      ConstantExpression(FloatResult(value.toDouble).asInstanceOf[EvaluationResult[T]])
    } else {
      ConstantExpression(IntResult(value.toInt).asInstanceOf[EvaluationResult[T]])
    }
  }

  // Builds a reference expression based on a cell reference like A1 or B2
  override def buildRef(ref: String): CellReferenceExpression[T] = {
    // At this point, the `ref` is guaranteed to be valid, because the error is treated in the Parser
    val cell = ParseTableCells.parse(ref).get
    CellReferenceExpression[T](cell)
  }

  // Builds a binary operation expression based on the operator and its operands
  override def buildBinaryOperation(left: Expression[T], right: Expression[T], op: String): Expression[T] = {
    op match {
      case "+" => AddExpression[T](left, right)
      case "-" => SubtractExpression[T](left, right)
      case "*" => MultiplyExpression[T](left, right)
      case "/" => DivideExpression[T](left, right)
    }
  }
}

