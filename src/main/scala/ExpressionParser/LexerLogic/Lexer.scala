package ExpressionParser.LexerLogic

import ExpressionParser._

import scala.collection.mutable.ListBuffer

// Lexer class responsible for tokenizing an input string into individual tokens
class Lexer(input: String, row: Int, col: Int) {

  private var pos = 0 // Position in the input string

  def nextToken(): Option[Tokens] = {
    skipWhitespaces()

    if (pos >= input.length) None
    else {
      input(pos) match {
        case c if c.isDigit => Some(parseNumber())
        case c if c.isLetter => Some(parseReference())
        case c if isOperator(c) => Some(parseOperator())
        case c => throw ExpressionParsingError(
          s"Unknown character at position $pos in cell ($row, $col): '$c' for the current expression $input"
        )
      }
    }
  }

  // Method to create the token list
  def tokenize(): List[Tokens] = {
    val tokens = ListBuffer[Tokens]()
    while (pos < input.length) {
      nextToken().foreach(tokens += _)
    }
    tokens.toList
  }

  //just keep the white spaces if there are any
  private def skipWhitespaces(): Unit = {
    while (pos < input.length && input(pos).isWhitespace) pos += 1
  }

  // Parse the number token
  private def parseNumber(): NumberToken = {
    val start = pos
    var hasDot = false

    while (pos < input.length && (input(pos).isDigit || (input(pos) == '.' && !hasDot))) {
      if (input(pos) == '.') hasDot = true
      pos += 1
    }

    NumberToken(input.substring(start, pos))
  }
  
  // Parse the ref token 
  private def parseReference(): RefToken = {
    val start = pos

    while (pos < input.length && input(pos).isLetterOrDigit) {
      pos += 1
    }

    RefToken(input.substring(start, pos))
  }
  
  //Parse the operator 
  private def parseOperator(): OperatorToken = {
    val operator = input(pos).toString
    pos += 1
    OperatorToken(operator)
  }
  
  // Method to see if the current operator is supported for our implementation 
  private def isOperator(char: Char): Boolean = Set('+', '-', '*', '/').contains(char)
}
