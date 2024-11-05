package ExpressionParserTest

import ExpressionParser.LexerLogic.{LeftParenthesis, Lexer, NumberToken, OperatorToken, RefToken, RightParenthesis, Tokens}
import org.scalatest.funsuite.AnyFunSuite

//Lexer testing
class LexerTest extends AnyFunSuite{

  test("test1"){

    val lexer = new Lexer("1+(2*3)")
    val list1 = lexer.tokenize()
    assert(list1 == List (
      NumberToken("1"),
      OperatorToken("+"),
      LeftParenthesis,
      NumberToken("2"),
      OperatorToken("*"),
      NumberToken("3"),
      RightParenthesis
    ))
  }

  test("test2") {

    val lexer = new Lexer("A1+B2+(C3/D12)/3")
    val list2 = lexer.tokenize()
    assert(list2 == List(
      RefToken("A1"),
      OperatorToken("+"),
      RefToken("B2"),
      OperatorToken("+"),
      LeftParenthesis,
      RefToken("C3"),
      OperatorToken("/"),
      RefToken("D12"),
      RightParenthesis,
      OperatorToken("/"),
      NumberToken("3")
    ))
  }

}
