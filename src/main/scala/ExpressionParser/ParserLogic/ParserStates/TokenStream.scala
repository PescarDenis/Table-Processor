package ExpressionParser.ParserLogic.ParserStates

import ExpressionParser.LexerLogic.{EndOfInputToken, Tokens}

//Case class used for the parser to iterate through the tokens
case class TokenStream(tokens: Seq[Tokens]) {
  def current: Tokens = tokens.headOption.getOrElse(EndOfInputToken) //method to get the current token, otherwise return the end
  def advance(): TokenStream = TokenStream(tokens.tail) //advance to the next tokens, but without the first one
  def isAtEnd: Boolean = tokens.isEmpty //to see if we reached the end of our parsing
}
