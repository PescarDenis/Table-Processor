package ExpressionParser.ParserLogic.ParserStates

import ExpressionAST.Expression
import ExpressionParser.ExpressionParsingError
import ExpressionParser.LexerLogic.*
import ExpressionParser.ParserLogic.ExpressionBuilder.ExpressionBuilderInterface
import TableParser.ParseTableCells

import scala.annotation.tailrec

//Parser class used for building the expression
case class Parser[T](tokens: Seq[Tokens], expressionBuilder: ExpressionBuilderInterface[T], row: Int, col: Int) {
  
  def parse(): Expression[T] = {
    if (tokens.isEmpty) {
      throw ExpressionParsingError(s"No valid expressions found in cell ($row, $col).") //if there is no expression to parse for example just = throw an error
    }
    val finalState = parseTokens(TokenStream(tokens), ParserState()) //get the final state and return the current expression that we get
    finalState.currentExpression.get //the errors are thrown internally
  }

  @tailrec //Annotation required by scala as we use recursion now
  private def parseTokens(tokenStream: TokenStream, state: ParserState[T]): ParserState[T] = {
    if (tokenStream.isAtEnd) { //If we reach the final token
      if (state.lastWasOperator) { //and the current state that we have ends with an operator
        throw ExpressionParsingError(s"Expression ends with an operator in cell ($row, $col).") // Throw an error in the error cell
      }
      return state // Also return the state as we have reached an end
    }

    //Now we go through each token and try to parse it.Also we use the combine expression to get the final expression that we want
    tokenStream.current match {
      case NumberToken(value) =>
        validateOperand(state.lastWasOperator, row, col)
        val numberExpression = expressionBuilder.buildConstant(value)
        val newState = state.copy(
          currentExpression = combineExpressions(state.currentExpression, numberExpression,state.lastOperator),
          lastWasOperator = false
        )
        parseTokens(tokenStream.advance(), newState)

      case RefToken(ref) =>
        validateOperand(state.lastWasOperator, row, col)
        ParseTableCells.parse(ref).getOrElse(
          throw ExpressionParsingError(s"Unsupported operand : '$ref' in cell ($row, $col).") // If there is an unknown operand in the expression throw an error
        )
        val refExpression = expressionBuilder.buildRef(ref)
        val newState = state.copy(
          currentExpression = combineExpressions(state.currentExpression, refExpression,state.lastOperator),
          lastWasOperator = false
        )
        parseTokens(tokenStream.advance(), newState)

      case OperatorToken(op) =>
        validateOperator(state.lastWasOperator, row, col)
        val newState = state.copy(
          lastWasOperator = true,
          lastOperator = op
        )
        parseTokens(tokenStream.advance(), newState)

      case EndOfInputToken => // This case will never be reached as the tokenize method does not tokenize it, it was used for the lexer so we know that we reached the final token
        state
    }
  }
  //Method to actually build and combine the final expression
  private def combineExpressions(current: Option[Expression[T]], next: Expression[T], lastOperator: String): Option[Expression[T]] = {
    current match {
      case None => Some(next)
      case Some(left) => Some(expressionBuilder.buildBinaryOperation(left, next, lastOperator))
    }
  }

  // Validation methods for operands and operators,if there is some error in the cell throw it
  private def validateOperand(lastWasOperator: Boolean, row: Int, col: Int): Unit = {
    if (!lastWasOperator) {
      throw ExpressionParsingError(s"Missing operator in cell ($row, $col).") //if we have a formula like =a b c -> it should throw an error of missing the operator
    }
  }

  private def validateOperator(lastWasOperator: Boolean, row: Int, col: Int): Unit = {
    if (lastWasOperator) {
      throw ExpressionParsingError(s"Unexpected operator in cell ($row, $col).") // if we introduce multiple operators it should throw an error
    }
  }
}
