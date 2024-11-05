package ExpressionParserTest

import Evaluation.EvaluationTypes.{FloatResult, IntResult}
import org.scalatest.funsuite.AnyFunSuite
import ExpressionAST.{ConstantExpression,CellReferenceExpression,AddExpression,SubtractExpression,MultiplyExpression,DivideExpression,EvaluationContext}
import ExpressionParser.LexerLogic.Lexer
import ExpressionParser.ParserLogic._
import Table.ParseTableCells
import Table.TableEntries.{Number,TableEntry}
class ParserTest extends AnyFunSuite {
  val expressionBuilder = new ExpressionBuilder[Any] // Use appropriate generic type if needed
  test("test1") {

    val context = new EvaluationContext(Map.empty) //empty evaluation context for testing
    val lexer = new Lexer("1+2*3")
    val tokens = lexer.tokenize()
    val parser = new Parser(tokens,expressionBuilder)
    val expression = parser.parse()

    val expressionAST = MultiplyExpression(
      AddExpression(ConstantExpression(IntResult(1)),ConstantExpression(IntResult(2))),ConstantExpression(IntResult(3)))
    assert(expressionAST == expression)
  }
  test("test2") {
    val lexer = new Lexer("2 * D4 / A3")
    val tokens = lexer.tokenize()
    val parser = new Parser(tokens,expressionBuilder)
    val expression = parser.parse()

    val expressionAST = DivideExpression(MultiplyExpression(
      ConstantExpression(IntResult(2)), // The constant 2
      CellReferenceExpression(ParseTableCells.parse("D4").get)),
      CellReferenceExpression(ParseTableCells.parse("A3").get)// Cell reference D4
    )

    assert(expression == expressionAST)

  }
  test("test3") {

    val context = new EvaluationContext(Map.empty) //empty evaluation context for testing

    val lexer = new Lexer("3+5*4/3")
    val tokens = lexer.tokenize()
    val parser = new Parser(tokens,expressionBuilder)
    val expression = parser.parse()


    // Now you can compare the AST structure
    val expressionAST = DivideExpression(
      MultiplyExpression(
        AddExpression(ConstantExpression(IntResult(3)), ConstantExpression(IntResult(5))),
        ConstantExpression(IntResult(4))
      ),
      ConstantExpression(IntResult(3))
    )

    assert(expression == expressionAST)

  }
  test("test4") {

    val lexer = new Lexer("A1 - 5 * B1 / 2")
    val tokens = lexer.tokenize()
    val parser = new Parser(tokens,expressionBuilder)
    val expression = parser.parse()

    // Evaluate the expression
    val expressionAST = DivideExpression(
      MultiplyExpression(SubtractExpression(
        CellReferenceExpression(ParseTableCells.parse("A1").get),ConstantExpression(IntResult(5))),
        CellReferenceExpression(ParseTableCells.parse("B1").get)),ConstantExpression(IntResult(2))
    )
    assert(expression == expressionAST)

  }
  test("test5"){
    val lexer = new Lexer("3++2")
    val tokens = lexer.tokenize()
    val parser = new Parser(tokens)
    val exception = intercept[IllegalArgumentException] {
      parser.parse() // This should throw an IllegalArgumentException
    }
    assert(exception.getMessage.contains("Unexpected operator at position"))
  }
}

