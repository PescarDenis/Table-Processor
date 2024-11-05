package ExpressionParser

import Evaluation.EvaluationTypes.{FloatResult, IntResult,EvaluationResult}
import ExpressionAST._
import Table.ParseTableCells
//Parser class -> used for creating the AST from a list of tokens
class Parser[T](tokens: List[Tokens]) {

  var pos = 0 // Index to track the position while parsing
  private var lastWasOperator = true // To track if the last token was an operator
  private var lastOperator: String = "" // Store the last operator encountered
  private var parsedExpression: Option[Expression[T]] = None // Store the parsed expression

  //parses the provided expression
  def parse(): Expression[T] = {
    if (tokens.isEmpty) throw new IllegalArgumentException("No tokens to parse")
    parseTokens()
  }
  //main parsing loop that processes each token based on its type
  private def parseTokens(): Expression[T] = {
    var currentExpression: Option[Expression[T]] = None // hold the current expression while parsing

    while (pos < tokens.length) {
      tokens(pos) match {
        case NumberToken(value) =>
          if (!lastWasOperator) {
            throw new IllegalArgumentException(s"Unexpected number at position $pos: '${value}'")
          }
          //create a constant expression based on whether the number is a float or an int
          val numberExpression: ConstantExpression[T] = if (value.contains('.')) {
            ConstantExpression(FloatResult(value.toDouble).asInstanceOf[EvaluationResult[T]])
          } else {
            ConstantExpression(IntResult(value.toInt).asInstanceOf[EvaluationResult[T]])
          }
          //integrate the number into the current expression
          currentExpression match {
            case None =>
              currentExpression = Some(numberExpression) // First number
            case Some(leftExpression) =>
              currentExpression = Some(createBinaryOperation(leftExpression, numberExpression, lastOperator))
          }
          lastWasOperator = false // next token should be an operator

        case RefToken(ref) =>
          if (!lastWasOperator) {
            throw new IllegalArgumentException(s"Unexpected reference at position $pos: '$ref'")
          }
          //same logic as before but to create the reference expression
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

  //Helper method to create binary operations, now supporting generic types
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
