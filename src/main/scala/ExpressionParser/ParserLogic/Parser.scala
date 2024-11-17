package ExpressionParser.ParserLogic

import ExpressionAST.Expression
import ExpressionParser.LexerLogic._

class Parser[T](tokens: List[Tokens], expressionBuilder: ExpressionBuilderInterface[T], row: Int, col: Int) {

  private var pos = 0
  private var lastWasOperator = true
  private var lastOperator: String = ""

  def parse(): Expression[T] = {
    if (tokens.isEmpty) {
      throw new IllegalArgumentException(s"No tokens to parse in cell ($row, $col).")
    }
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

      }
      pos += 1
    }

    if (lastWasOperator) {
      throw new IllegalArgumentException(s"Expression ends with an operator in cell ($row, $col).")
    }

    currentExpression.getOrElse(
      throw new IllegalArgumentException(s"No valid expressions found in cell ($row, $col).")
    )
  }

  private def combineExpressions(current: Option[Expression[T]], next: Expression[T]): Option[Expression[T]] = {
    current match {
      case None        => Some(next)
      case Some(left)  => Some(expressionBuilder.buildBinaryOperation(left, next, lastOperator))
    }
  }

  private def validateOperand(): Unit = {
    if (!lastWasOperator) {
      throw new IllegalArgumentException(s"Unexpected operand at position $pos in cell ($row, $col).")
    }
  }

  private def validateOperator(): Unit = {
    if (lastWasOperator) {
      throw new IllegalArgumentException(s"Unexpected operator at position $pos in cell ($row, $col).")
    }
  }
}

