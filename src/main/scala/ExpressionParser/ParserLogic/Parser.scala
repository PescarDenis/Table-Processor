package ExpressionParser.ParserLogic

import ExpressionAST.Expression
import ExpressionParser.LexerLogic.*
import TableParser.ParseTableCells

//Parser class used for building the expression
class Parser[T](tokens: List[Tokens], expressionBuilder: ExpressionBuilderInterface[T], row: Int, col: Int) {

  private var pos = 0 //start pos in the parser
  private var lastWasOperator = true
  private var lastOperator: String = ""

  def parse(): Expression[T] = {
    if (tokens.isEmpty) {
      throw new IllegalArgumentException(s"No valid expressions found in cell ($row, $col).") //if there is no expression to parse for example just = throw an error
    }
    parseTokens() // Parse the tokens
  }

  // Private method to iterate throguh the tokens and create the wanted expression
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
          if (!ParseTableCells.parse(ref).isDefined) {
            throw new IllegalArgumentException(
              s"Unsupported operand : '$ref' in cell ($row, $col)." // If we have an expression and there is an unknown  operand throw an error
            )
          }
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
      throw new IllegalArgumentException(s"Expression ends with an operator in cell ($row, $col).") // If the expression ends with an operator
    }
    else {
      currentExpression.get
    }
  }

  //Method to actually build and combine the final expression
  private def combineExpressions(current: Option[Expression[T]], next: Expression[T]): Option[Expression[T]] = {
    current match {
      case None        => Some(next)
      case Some(left)  => Some(expressionBuilder.buildBinaryOperation(left, next, lastOperator))
    }
  }

  private def validateOperand(): Unit = {
    if (!lastWasOperator) {
      throw new IllegalArgumentException(s"Unexpected operand in the token list at position $pos in cell ($row, $col).")
    }
  }

  private def validateOperator(): Unit = {
    if (lastWasOperator) {
      throw new IllegalArgumentException(s"Unexpected operator in the token list at position $pos in cell ($row, $col).")
    }
  }
}

