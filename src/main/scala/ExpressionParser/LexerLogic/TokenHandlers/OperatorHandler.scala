package ExpressionParser.LexerLogic.TokenHandlers
import ExpressionParser.LexerLogic.{LexerState, OperatorToken, Tokens}

case class OperatorHandler (operators : Set[Char]) extends TokenHandler {
  override def parse(state: LexerState): Option[Tokens] = {
    // no need for a while as we only have one operator in a formula
    if(state.hasMore && operators.contains(state.currentChar.get)){
      val operator = state.currentChar.get.toString
      state.advance()
      Some(OperatorToken(operator))
    }
    else None
  }
}
