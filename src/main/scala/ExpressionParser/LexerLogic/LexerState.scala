package ExpressionParser.LexerLogic

//Create a lexer state for managing how we traverse the tokens and how to lexerize the expression
// Improvements for using a real OOP paradigm
class LexerState(val input: String) { // take the tokens as a string
  var pos: Int = 0 //initially position is 0

  def currentChar: Option[Char] = if (pos < input.length) Some(input(pos)) else None //gets the current 

  def advance(): Unit = if (pos < input.length) pos += 1 // used for advancing in the string

  def hasMore: Boolean = pos < input.length // to see if we have more tokens

  def slice(start: Int, end: Int): String = input.slice(start, end) //slice is used to get the token from its certain positions 
}

