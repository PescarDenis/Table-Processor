package ExpressionParser.ParserLogic


import Evaluation.EvaluationTypes.{EvaluationResult, FloatResult, IntResult}
import ExpressionAST.*
import Table.ParseTableCells

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
    ParseTableCells.parse(ref) match {
      case Some(cell) => CellReferenceExpression[T](cell)
      case None => throw new IllegalArgumentException(s"Invalid cell reference: $ref")
    }
  }

  // Builds a binary operation expression based on the operator and its operands
  override def buildBinaryOperation(left: Expression[T], right: Expression[T], op: String): Expression[T] = {
    op match {
      case "+" => AddExpression[T](left, right)
      case "-" => SubtractExpression[T](left, right)
      case "*" => MultiplyExpression[T](left, right)
      case "/" => DivideExpression[T](left, right)
      case _ => throw new IllegalArgumentException(s"Unknown operator: $op")
    }
  }
}

