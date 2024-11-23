package ExpressionParser.LexerLogic

import ExpressionParser.ExpressionParsingError
import ExpressionParser.LexerLogic.TokenHandlers.{TokenHandler,NumberHandler,ReferenceHandler,OperatorHandler}

// Define the actual Lexer classs
class Lexer(input: String,row : Int, col : Int) {

  // Internal definition of supported operators
  private val operators: Set[Char] = Set('+', '-', '*', '/')

  private val state = LexerState(input) //C
  private val handlers: Seq[TokenHandler] = Seq(
    new NumberHandler,
    new ReferenceHandler,
    OperatorHandler(operators)
  ) // Define the sequence of handlers that we have in our current configuration

  // Method to get the next token
  private def nextToken(): Tokens = {
    skipWhitespaces()  //skip the white spaces

    if (!state.hasMore) EndOfInputToken //if we reached the end it means that there are no tokens left
    else {
      // Try to parse each token and return the first occurrence of a valid token
      handlers.iterator.flatMap(_.parse(state)).nextOption().getOrElse {
          throw ExpressionParsingError(
            s"Unknown character '${state.currentChar.get}' at position ${state.pos} in cell ($row, $col) for the current expression $input" //if we did not get a valid token throw an error
          )
        }
    }
  }

  // Helper method to skip the whitespaces
  private def skipWhitespaces(): Unit = {
    while (state.hasMore && state.currentChar.get.isWhitespace) state.advance()
  }

  def tokenize(): Seq[Tokens] = {
    Iterator.continually(nextToken()) // Create an infinite iterator that keeps calling the next token method to access the valid tokens
      .takeWhile { // Process the tokens until we reach the end
        case EndOfInputToken => false
        case _               => true
      } .toSeq //convert it back to a Seq
  }
  
}
