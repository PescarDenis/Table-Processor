package ExpressionParserTest

import ExpressionParser.ExpressionParsingError
import ExpressionParser.LexerLogic.{Lexer, NumberToken, OperatorToken, RefToken, Tokens}
import org.scalatest.funsuite.AnyFunSuite

//Lexer testing
class LexerTest extends AnyFunSuite{

  test("Normal lexer test without any ref.Only numbers and operators"){

    val lexer = new Lexer("1+2*3",1,1)
    val tokens = lexer.tokenize()
    assert(tokens == List (
      NumberToken("1"),
      OperatorToken("+"),
      NumberToken("2"),
      OperatorToken("*"),
      NumberToken("3"),
    ))
  }

  test("Test with the cell referencing,numbers and operators") {

    val lexer = new Lexer("A1+B2+C3/D12/3",213,1231)
    val tokens = lexer.tokenize()
    assert(tokens == List(
      RefToken("A1"),
      OperatorToken("+"),
      RefToken("B2"),
      OperatorToken("+"),
      RefToken("C3"),
      OperatorToken("/"),
      RefToken("D12"),
      OperatorToken("/"),
      NumberToken("3")
    ))
  }

  test("Test with an an unknown token at a position"){
    val lexer = new Lexer("A(+3" ,1 ,1)
    val thrownException = intercept[ExpressionParsingError]  {
      val tokens = lexer.tokenize()
    }
    assert(thrownException.getMessage.contains("Unknown character at position 1 in cell (1, 1): '('" +
      " for the current expression A(+3"))
  }

}
