package EvaluationTest

import Evaluation.EvaluationTypes.{EmptyResult, EvaluationError, FloatResult, IntResult}
import org.scalatest.funsuite.AnyFunSuite
import Table.TableEntries.{Empty, Formula, Number}
import ExpressionAST.EvaluationContext
import Table.DefinedTabels.BaseTable
import TableParser.{FileParser, ParseTableCells}
import ExpressionParser.ParserLogic.ExpressionBuilder
import ExpressionParser.ParsingServices.DefaultExpressionParser

class TableEvaluatorTest extends AnyFunSuite {

  // Create an instance of the ExpressionParser to inject into Formulas
  private val expressionParser = new DefaultExpressionParser(new ExpressionBuilder)

  test("General Table evaluation scenario with formula referencing an empty cell and EvaluationError") {
    val context = new EvaluationContext(Map(
      ParseTableCells(4, 6) ->  Formula(4, 6, expressionParser),
      ParseTableCells(2, 3) -> Number(2, 3),
      ParseTableCells(11, 17) -> Number(11, 17),
      ParseTableCells(13, 6) -> Empty(13, 6),
      ParseTableCells(2, 1) -> Formula(2, 1, expressionParser)
    ))

    context.lookup(ParseTableCells(2, 3)).set("18") // C2 = 18
    context.lookup(ParseTableCells(11, 17)).set("4") // Q11 = 4
    context.lookup(ParseTableCells(4, 6)).set("=C2 * F13")
    context.lookup(ParseTableCells(2, 1)).set("=Q11 / C2")

    val table = new BaseTable(new FileParser(expressionParser))
    table.initializeRows(context.getTable)
    table.evaluateAllCells(context)

    assert(table.getEvaluatedResult(ParseTableCells(2, 3)).contains(IntResult(18)))
    assert(table.getEvaluatedResult(ParseTableCells(11, 17)).contains(IntResult(4)))
    assert(table.getEvaluatedResult(ParseTableCells(2, 1)).contains(FloatResult(0.2222222222222222)))
    assert(table.getEvaluatedResult(ParseTableCells(4, 6)).exists(_.isInstanceOf[EvaluationError]))
  }

  test("Plain reference to an empty cell") {
    val context = new EvaluationContext(Map(
      ParseTableCells(1, 1) ->  Formula(1, 1, expressionParser),
      ParseTableCells(1, 2) -> Empty(1, 2)
    ))

    context.lookup(ParseTableCells(1, 1)).set("=B1") // A1 = B1

    val table = new BaseTable(new FileParser(expressionParser))
    table.initializeRows(context.getTable)
    table.evaluateAllCells(context)

    assert(table.getEvaluatedResultAsString(ParseTableCells(1, 1)) == "")
  }

  test("Circular dependency: Formula references itself") {
    val context = new EvaluationContext(Map(
      ParseTableCells(1, 1) ->  Formula(1, 1, expressionParser)
    ))

    context.lookup(ParseTableCells(1, 1)).set("=A1 + 1")

    val table = new BaseTable(new FileParser(expressionParser))
    table.initializeRows(context.getTable)

    val thrownException = intercept[IllegalArgumentException] {
      table.evaluateAllCells(context)
    }

    assert(thrownException.getMessage.contains("Circular dependency detected at cell: A1"))
  }

  test("Circular dependency: Formulas referencing each other") {
    val context = new EvaluationContext(Map(
      ParseTableCells(1, 1) ->  Formula(1, 1, expressionParser),
      ParseTableCells(1, 2) ->  Formula(1, 2, expressionParser)
    ))

    context.lookup(ParseTableCells(1, 1)).set("=B1 + 1")
    context.lookup(ParseTableCells(1, 2)).set("=A1 + 1")

    val table = new BaseTable(new FileParser(expressionParser))
    table.initializeRows(context.getTable)

    val thrownException = intercept[IllegalArgumentException] {
      table.evaluateAllCells(context)
    }

    assert(thrownException.getMessage.contains("Circular dependency detected at cell: B1"))
  }

  test("Table evaluator with no problems") {
    val context = new EvaluationContext(Map(
      ParseTableCells(4, 6) ->  Formula(4, 6, expressionParser),
      ParseTableCells(2, 3) -> Number(2, 3),
      ParseTableCells(11, 17) -> Number(11, 17),
      ParseTableCells(13, 6) -> Empty(13, 6),
      ParseTableCells(2, 1) ->  Formula(2, 1, expressionParser)
    ))

    context.lookup(ParseTableCells(2, 3)).set("18")
    context.lookup(ParseTableCells(11, 17)).set("4")
    context.lookup(ParseTableCells(4, 6)).set("=C2 * A2")
    context.lookup(ParseTableCells(2, 1)).set("=Q11 / C2")

    val table = new BaseTable(new FileParser(expressionParser))
    table.initializeRows(context.getTable)
    table.evaluateAllCells(context)

    assert(table.getEvaluatedResult(ParseTableCells(2, 3)).contains(IntResult(18)))
    assert(table.getEvaluatedResult(ParseTableCells(11, 17)).contains(IntResult(4)))
    assert(table.getEvaluatedResult(ParseTableCells(2, 1)).contains(FloatResult(0.2222222222222222)))
    assert(table.getEvaluatedResult(ParseTableCells(4, 6)).contains(FloatResult(4.0)))
    assert(table.getEvaluatedResult(ParseTableCells(13, 6)).contains(EmptyResult))
  }

  test("Evaluate cells and retrieve evaluated results as strings") {
    val context = new EvaluationContext(Map(
      ParseTableCells(1, 1) -> Number(1, 1),
      ParseTableCells(1, 2) ->  Formula(1, 2, expressionParser),
      ParseTableCells(2, 1) ->  Formula(2, 1, expressionParser),
      ParseTableCells(2, 2) -> Empty(2, 2)
    ))

    context.lookup(ParseTableCells(1, 1)).set("10")
    context.lookup(ParseTableCells(1, 2)).set("=A1 + 5")
    context.lookup(ParseTableCells(2, 1)).set("=B1 / 2")

    val table = new BaseTable(new FileParser(expressionParser))
    table.initializeRows(context.getTable)
    table.evaluateAllCells(context)

    assert(table.getEvaluatedResultAsString(ParseTableCells(1, 1)) == "10")
    assert(table.getEvaluatedResultAsString(ParseTableCells(1, 2)) == "15")
    assert(table.getEvaluatedResultAsString(ParseTableCells(2, 1)) == "7.5")
    assert(table.getEvaluatedResultAsString(ParseTableCells(2, 2)) == "")
  }
}
