package ExpressionParserTest

import Evaluation.EvaluationTypes.{FloatResult, IntResult}
import org.scalatest.funsuite.AnyFunSuite
import ExpressionAST.{ConstantExpression, CellReferenceExpression, AddExpression, SubtractExpression, MultiplyExpression, DivideExpression, EvaluationContext}
import ExpressionParser.LexerLogic.Lexer
import ExpressionParser.ParserLogic.{ExpressionBuilder, Parser}
import TableParser.ParseTableCells

class ParserTest extends AnyFunSuite {

  val expressionBuilder = new ExpressionBuilder[Any] // Update with a specific type if required
  var row  = 1 //declare a row and a col for testing purposes
  var col = 2
  test("test1") {
    val lexer = new Lexer("1+2*3",row,col)
    val tokens = lexer.tokenize()
    val parser = new Parser(tokens, expressionBuilder,row,col)
    val expression = parser.parse()

    // Expected AST: (1 + 2) * 3
    val expectedAST = MultiplyExpression(
      AddExpression(
        ConstantExpression(IntResult(1)),
        ConstantExpression(IntResult(2))
      ),
      ConstantExpression(IntResult(3))
    )

    assert(expression == expectedAST)
  }

  test("test2") {
    val lexer = new Lexer("2 * D4 / A3",row,col)
    val tokens = lexer.tokenize()
    val parser = new Parser(tokens, expressionBuilder,row,col)
    val expression = parser.parse()

    // Expected AST: (2 * D4) / A3
    val expectedAST = DivideExpression(
      MultiplyExpression(
        ConstantExpression(IntResult(2)),
        CellReferenceExpression(ParseTableCells.parse("D4").get)
      ),
      CellReferenceExpression(ParseTableCells.parse("A3").get)
    )

    assert(expression == expectedAST)
  }

  test("test3") {
    val lexer = new Lexer("3+5*4/3",row,col)
    val tokens = lexer.tokenize()
    val parser = new Parser(tokens, expressionBuilder,row,col)
    val expression = parser.parse()

    // Expected AST: ((3 + 5) * 4) / 3
    val expectedAST = DivideExpression(
      MultiplyExpression(
        AddExpression(
          ConstantExpression(IntResult(3)),
          ConstantExpression(IntResult(5))
        ),
        ConstantExpression(IntResult(4))
      ),
      ConstantExpression(IntResult(3))
    )

    assert(expression == expectedAST)
  }

  test("test4") {
    val lexer = new Lexer("A1 - 5 * B1 / 2",row,col)
    val tokens = lexer.tokenize()
    val parser = new Parser(tokens, expressionBuilder,row,col)
    val expression = parser.parse()

    // Expected AST: ((A1 - 5) * B1) / 2
    val expectedAST = DivideExpression(
      MultiplyExpression(
        SubtractExpression(
          CellReferenceExpression(ParseTableCells.parse("A1").get),
          ConstantExpression(IntResult(5))
        ),
        CellReferenceExpression(ParseTableCells.parse("B1").get)
      ),
      ConstantExpression(IntResult(2))
    )

    assert(expression == expectedAST)
  }

  test("test5") {
    val lexer = new Lexer("3++2",row,col)
    val tokens = lexer.tokenize()
    val parser = new Parser(tokens, expressionBuilder,row,col)

    val exception = intercept[IllegalArgumentException] {
      parser.parse()
    }
    val pos = 2
    assert(exception.getMessage.contains(s"Unexpected operator at position $pos in cell ($row, $col)."))
  }
}
