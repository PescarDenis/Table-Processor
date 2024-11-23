package ExpressionParser.LexerLogic.TokenHandlers
import ExpressionParser.LexerLogic.{LexerState, RefToken, Tokens}

class ReferenceHandler extends TokenHandler {
  override def parse(state: LexerState): Option[Tokens] = {

    val start = state.pos
    
    //if there are still elements in the string and we find a digit or a letter advance
    while(state.hasMore && state.currentChar.get.isLetterOrDigit){
      state.advance()
    }
    if (start != state.pos) Some(RefToken(state.slice(start, state.pos))) else None

  }
}
