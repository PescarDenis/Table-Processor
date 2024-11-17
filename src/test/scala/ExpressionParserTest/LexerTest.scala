package ExpressionParserTest

import ExpressionParser.LexerLogic.{ Lexer, NumberToken, OperatorToken, RefToken, Tokens}
import org.scalatest.funsuite.AnyFunSuite

//Lexer testing
class LexerTest extends AnyFunSuite{

  test("test1"){

    val lexer = new Lexer("1+2*3",1,1)
    val list1 = lexer.tokenize()
    assert(list1 == List (
      NumberToken("1"),
      OperatorToken("+"),
      NumberToken("2"),
      OperatorToken("*"),
      NumberToken("3"),
    ))
  }

  test("test2") {

    val lexer = new Lexer("A1+B2+C3/D12/3",213,1231)
    val list2 = lexer.tokenize()
    assert(list2 == List(
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

}
