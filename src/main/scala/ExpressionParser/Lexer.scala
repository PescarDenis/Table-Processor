package ExpressionParser

import scala.collection.mutable.ListBuffer

// Lexer class responsible for tokenizing an input string into individual tokens
class Lexer (input : String){

  var pos = 0 // Position in the input string, used to iterate through the characters

  def NextToken() : Option[Tokens] = {
    skipwhitespaces() //skip the white spaces

    //check if the end of the input has been reached
    if(pos >= input.length) {
      None
    }
    else {
      val curr = input(pos) //current char in the input string

      curr match {
        //case for numbers
        case c if c.isDigit =>
          val start = pos //initial starting pos
          var hasDot = false //flag to track if a decimal point is present
          while (pos < input.length && (input(pos).isDigit || (input(pos) == '.' && !hasDot))) {
            if (input(pos) == '.') hasDot = true
            pos = pos + 1
          }
          Some(NumberToken(input.substring(start, pos))) //create a number token

        case c if c.isLetter =>
          val start = pos
          while (pos < input.length && (input(pos).isDigit || input(pos).isLetter)) pos = pos + 1
          Some(RefToken(input.substring(start, pos)))


        case '+' | '-' | '*' | '/' => //if it is an operator
          pos = pos + 1 //go to the next position
          Some(OperatorToken(curr.toString)) //represent it with the OperatorToken


        case '(' =>
          pos = pos + 1 //go to the next position
          Some(LeftParenthesis) //tokenize it


        case ')' =>
          pos = pos + 1
          Some(RightParenthesis)

        case _ =>
          throw new IllegalArgumentException(s"Unexpected character : '$curr")
      }
    }

  }

  private def skipwhitespaces() : Unit ={
    while(pos < input.length && input(pos).isWhitespace) pos = pos + 1 //helper function to skip white spaces
  }

  //tokenize the input
  def tokenize() : List[Tokens] = {
    val tokens = ListBuffer[Tokens]()

    while(pos <  input.length){
      NextToken() match {
        case Some(token) => tokens +=  token
        case None => //End of input
      }
    }
    tokens.toList
  }
}

