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
      val currentChar = input(pos)
      if (currentChar.isDigit) {
        Some(parseNumber())
      } else if (currentChar.isLetter) {
        Some(parseReference())
      } else if (isOperator(currentChar)) {
        Some(parseOperator())
      } else {
        throw new IllegalArgumentException(
          s"Unexpected character at position $pos in cell ($row, $col): '$currentChar' for the current expression $input"
        )
      }
    }
  }

  def tokenize(): List[Tokens] = {
    val tokens = ListBuffer[Tokens]()
    while (pos < input.length) {
      nextToken().foreach(tokens += _)
    }
    tokens.toList
  }

  private def skipWhitespaces(): Unit = {
    while (pos < input.length && input(pos).isWhitespace) pos += 1
  }

  private def parseNumber(): NumberToken = {
    val start = pos
    var hasDot = false

    while (pos < input.length && (input(pos).isDigit || (input(pos) == '.' && !hasDot))) {
      if (input(pos) == '.') hasDot = true
      pos += 1
    }

    NumberToken(input.substring(start, pos))
  }

  private def parseReference(): RefToken = {
    val start = pos

    while (pos < input.length && input(pos).isLetterOrDigit) {
      pos += 1
    }

    RefToken(input.substring(start, pos))
  }

  private def parseOperator(): OperatorToken = {
    val operator = input(pos).toString
    pos += 1
    OperatorToken(operator)
  }

  private def isOperator(char: Char): Boolean = Set('+', '-', '*', '/').contains(char)
}
