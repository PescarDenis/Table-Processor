package ExpressionParser.LexerLogic.TokenHandlers
import ExpressionParser.LexerLogic.{LexerState, NumberToken, Tokens}

class NumberHandler extends TokenHandler {
  override def parse(state: LexerState): Option[Tokens] = {
    val start = state.pos //take the starting pos of the current token
    var hasDot = false //providing that the current token does is to float

    //if we can advance and the current char is either a digit or a '.'(it means that
    //we found a float number)
    while(state.hasMore && (state.currentChar.get.isDigit ||
                            (state.currentChar.get == '.' && !hasDot))){
      if(state.currentChar.get == '.') hasDot = true //make it true
      state.advance() //go to the next char
    }

    // if we indeed find a valid token create it, otherwise return none as we did not find it 
    if(start != state.pos) Some(NumberToken(state.slice(start,state.pos))) else None
  }
}
