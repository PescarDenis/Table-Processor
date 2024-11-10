package ExpressionParser.ParserLogic

import ExpressionAST.Expression
import ExpressionParser.LexerLogic._

class Parser[T](tokens: List[Tokens], expressionBuilder: ExpressionBuilderInterface[T]) {

  private var pos = 0
  private var lastWasOperator = true
  private var lastOperator: String = ""

  def parse(): Expression[T] = {
    if (tokens.isEmpty) throw new IllegalArgumentException("No tokens to parse")
    parseTokens()
  }

  private def parseTokens(): Expression[T] = {
    var currentExpression: Option[Expression[T]] = None

    while (pos < tokens.length) {
      tokens(pos) match {
        case NumberToken(value) =>
          validateOperand()
          val numberExpression = expressionBuilder.buildConstant(value)
          currentExpression = combineExpressions(currentExpression, numberExpression)
          lastWasOperator = false

        case RefToken(ref) =>
          validateOperand()
          val refExpression = expressionBuilder.buildRef(ref)
          currentExpression = combineExpressions(currentExpression, refExpression)
          lastWasOperator = false

        case OperatorToken(op) =>
          validateOperator()
          lastOperator = op
          lastWasOperator = true

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

  private def combineExpressions(current: Option[Expression[T]], next: Expression[T]): Option[Expression[T]] = {
    current match {
      case None        => Some(next)
      case Some(left)  => Some(expressionBuilder.buildBinaryOperation(left, next, lastOperator))
    }
  }

  private def validateOperand(): Unit = {
    if (!lastWasOperator) throw new IllegalArgumentException(s"Unexpected operand at position $pos")
  }

  private def validateOperator(): Unit = {
    if (lastWasOperator) throw new IllegalArgumentException(s"Unexpected operator at position $pos")
  }
}
