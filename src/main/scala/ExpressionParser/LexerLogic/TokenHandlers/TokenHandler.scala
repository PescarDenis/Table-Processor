package ExpressionParser.LexerLogic.TokenHandlers

import ExpressionParser.LexerLogic.{LexerState, Tokens}

//Define a trait for handling the tokens 
//it takes the lexer state as an input and returns a token if there is any
trait TokenHandler {
  def parse(state: LexerState): Option[Tokens]
}


