package ExpressionParser.ParserLogic

import Evaluation.EvaluationTypes.{EvaluationResult, FloatResult, IntResult}
import ExpressionAST.*
import ExpressionParser.LexerLogic.*

// Parser class -> used for creating the AST from a list of tokens
//use dependency injection to inject the builder interface, this way the code is truly modular 
class Parser[T](tokens: List[Tokens], expressionBuilder: ExpressionBuilderInterface[T]) {

  var pos = 0 // Index to track the position while parsing
  private var lastWasOperator = true // To track if the last token was an operator
  private var lastOperator: String = "" // Store the last operator encountered
  private var parsedExpression: Option[Expression[T]] = None // Store the parsed expression


  // Parses the provided expression
  def parse(): Expression[T] = {
    if (tokens.isEmpty) throw new IllegalArgumentException("No tokens to parse")
    parseTokens()
  }

  // Main parsing loop that processes each token based on its type
  private def parseTokens(): Expression[T] = {
    var currentExpression: Option[Expression[T]] = None // Hold the current expression while parsing

    while (pos < tokens.length) {
      tokens(pos) match {
        case NumberToken(value) =>
          validateOperand()
          val numberExpression = expressionBuilder.buildConstant(value) // Use ExpressionBuilder to build constant
          currentExpression = combineExpressions(currentExpression, numberExpression)
          lastWasOperator = false // Next token should be an operator

        case RefToken(ref) =>
          validateOperand()
          val refExpression = expressionBuilder.buildRef(ref) // Use ExpressionBuilder to build reference
          currentExpression = combineExpressions(currentExpression, refExpression)
          lastWasOperator = false // Next token should be an operator

        case OperatorToken(op) =>
          validateOperator()
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

  // Combines the current and next expressions using the last operator
  private def combineExpressions(current: Option[Expression[T]], next: Expression[T]): Option[Expression[T]] = {
    current match {
      case None => Some(next) // First expression
      case Some(left) => Some(expressionBuilder.buildBinaryOperation(left, next, lastOperator))
    }
  }

  // Validates that the current token is a valid operand
  private def validateOperand(): Unit = {
    if (!lastWasOperator) {
      throw new IllegalArgumentException(s"Unexpected operand at position $pos")
    }
  }

  // Validates that the current token is a valid operator
  private def validateOperator(): Unit = {
    if (lastWasOperator) {
      throw new IllegalArgumentException(s"Unexpected operator at position $pos")
    }
  }
}
