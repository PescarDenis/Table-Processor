package ExpressionParser

import Evaluation.EvaluationTypes.{FloatResult, IntResult,EvaluationResult}
import ExpressionAST._
import Table.ParseTableCells

class Parser[T](tokens: List[Tokens]) {

  var pos = 0 // Index to track the position while parsing
  private var lastWasOperator = true // To track if the last token was an operator
  private var lastOperator: String = "" // Store the last operator encountered
  private var parsedExpression: Option[Expression[T]] = None // Store the parsed expression

  def parse(): Expression[T] = {
    if (tokens.isEmpty) throw new IllegalArgumentException("No tokens to parse")
    parseTokens()
  }

  private def parseTokens(): Expression[T] = {
    var currentExpression: Option[Expression[T]] = None // Use Option to hold the current expression

    while (pos < tokens.length) {
      tokens(pos) match {
        case NumberToken(value) =>
          if (!lastWasOperator) {
            throw new IllegalArgumentException(s"Unexpected number at position $pos: '${value}'")
          }

          val numberExpression: ConstantExpression[T] = if (value.contains('.')) {
            ConstantExpression(FloatResult(value.toDouble).asInstanceOf[EvaluationResult[T]])
          } else {
            ConstantExpression(IntResult(value.toInt).asInstanceOf[EvaluationResult[T]])
          }

          currentExpression match {
            case None =>
              currentExpression = Some(numberExpression) // First number
            case Some(leftExpression) =>
              currentExpression = Some(createBinaryOperation(leftExpression, numberExpression, lastOperator))
          }
          lastWasOperator = false // Expect operator next

        case RefToken(ref) =>
          if (!lastWasOperator) {
            throw new IllegalArgumentException(s"Unexpected reference at position $pos: '$ref'")
          }

          ParseTableCells.parse(ref) match {
            case Some(cell) =>
              val refExpression = CellReferenceExpression[T](cell)
              currentExpression match {
                case None =>
                  currentExpression = Some(refExpression) // First reference
                case Some(leftExpression) =>
                  currentExpression = Some(createBinaryOperation(leftExpression, refExpression, lastOperator))
              }
              lastWasOperator = false // Expect operator next
            case None =>
              throw new IllegalArgumentException(s"Invalid cell reference: $ref")
          }

        case OperatorToken(op) =>
          if (lastWasOperator) {
            throw new IllegalArgumentException(s"Unexpected token at position $pos")
          }

          lastOperator = op // Save the last operator
          lastWasOperator = true // Expect operand next

        case _ =>
          throw new IllegalArgumentException(s"Unexpected token at position $pos")
      }
      pos += 1
    }

    if (lastWasOperator) {
      throw new IllegalArgumentException("Expression ends with an operator")
    }

    currentExpression.getOrElse(throw new IllegalArgumentException("No valid expressions found"))
  }

  // Helper method to create binary operations, now supporting generic types
  private def createBinaryOperation(left: Expression[T], right: Expression[T], op: String): Expression[T] = {
    op match {
      case "+" => AddExpression[T](left, right)
      case "-" => SubtractExpression[T](left, right)
      case "*" => MultiplyExpression[T](left, right)
      case "/" => DivideExpression[T](left, right)
      case _ => throw new IllegalArgumentException(s"Unknown operator: $op")
    }
  }
}
