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
  test("Normal Parser") {
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

  test("Normal parser with ref token") {
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

  test("Normal parser again, but with more operators") {
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

  test("Normal Parser operators + numbers + ref tokens") {
    val lexer = new Lexer("A1 - 5.27 * B1 / 2",row,col)
    val tokens = lexer.tokenize()
    val parser = new Parser(tokens, expressionBuilder,row,col)
    val expression = parser.parse()

    val expectedAST = DivideExpression(
      MultiplyExpression(
        SubtractExpression(
          CellReferenceExpression(ParseTableCells.parse("A1").get),
          ConstantExpression(FloatResult(5.27))
        ),
        CellReferenceExpression(ParseTableCells.parse("B1").get)
      ),
      ConstantExpression(IntResult(2))
    )

    assert(expression == expectedAST)
  }


  test("Unexpected operator in the middle") {
    val lexer = new Lexer("A3++2",row,col)
    val tokens = lexer.tokenize()
    val parser = new Parser(tokens, expressionBuilder,row,col)

    val exception = intercept[IllegalArgumentException] {
      parser.parse()
    }
    val pos = 2
    assert(exception.getMessage.contains(s"Unexpected operator in the token list at position $pos in cell ($row, $col)."))
  }

  test("Unexpected operator of the expression at the end"){
    val lexer = new Lexer("2*A1-2/3-",row,col)
    val tokens = lexer.tokenize()
    val parser = new Parser(tokens,expressionBuilder,row,col)

    val exception = intercept[IllegalArgumentException] {
      parser.parse()
    }
    val pos = 1
    assert(exception.getMessage.contains(s"Expression ends with an operator in cell ($row, $col)."))

  }
  test("Unexpected operand at some random position") {
    val lexer = new Lexer("A3/B1231+2 4", row, col)
    val tokens = lexer.tokenize()
    val parser = new Parser(tokens, expressionBuilder, row, col)

    val exception = intercept[IllegalArgumentException] {
      parser.parse()
    }
    val pos = 5
    assert(exception.getMessage.contains(s"Unexpected operand in the token list at position $pos in cell ($row, $col)."))
  }
  test("No valid cell reference in the table"){
    val lexer = new Lexer("a1231-2",row,col)
    val tokens = lexer.tokenize()
    val parser = new Parser(tokens,expressionBuilder,row,col)

    val exception = intercept[IllegalArgumentException] {
      parser.parse()
    }

    assert(exception.getMessage.contains("There is no cell found in the table with the given reference : a1231"))

  }
  test("No valid expression found in the cell") {
    val lexer = new Lexer(" ", row, col) // Empty input
    val tokens = lexer.tokenize()
    val parser = new Parser(tokens, expressionBuilder, row, col)

    val exception = intercept[IllegalArgumentException] {
      parser.parse()
    }

    assert(exception.getMessage.contains(s"No valid expressions found in cell ($row, $col)."))
  }
}
