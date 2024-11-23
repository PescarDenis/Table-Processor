package EvaluationTest

import Evaluation.EvaluationTypes.{EmptyResult, FloatResult, IntResult}
import Evaluation.TableEvaluatorError
import org.scalatest.funsuite.AnyFunSuite
import Table.TableEntries.{Empty, Formula, Number, TableEntry}
import ExpressionAST.EvaluationContext
import ExpressionParser.ParserLogic.ExpressionBuilder.ExpressionBuilder
import Table.DefinedTabels.BaseTable
import TableParser.{FileParser, ParseTableCells}
import ExpressionParser.ParsingServices.DefaultExpressionParser
import Table.TableModel
class TableEvaluatorTest extends AnyFunSuite {

  // Create an instance of the ExpressionParser to inject into Formulas
  private val expressionParser = new DefaultExpressionParser(new ExpressionBuilder)

  test("General Table evaluation scenario with formula referencing an empty cell and EvaluationError") {
    val initialData = Map(
      ParseTableCells(4, 6) -> Formula(4, 6, expressionParser),
      ParseTableCells(2, 3) -> Number(2, 3),
      ParseTableCells(11, 17) -> Number(11, 17),
      ParseTableCells(13, 6) -> Empty(13, 6),
      ParseTableCells(2, 1) -> Formula(2, 1, expressionParser)
    )

    val tableModel = new TableModel(initialData) // Convert to TableModel
    val table = new BaseTable(new FileParser(expressionParser))
    table.initializeRows(tableModel)

    val context = new EvaluationContext(table)

    context.lookup(ParseTableCells(2, 3)).set("18") // C2 = 18
    context.lookup(ParseTableCells(11, 17)).set("4") // Q11 = 4
    context.lookup(ParseTableCells(4, 6)).set("=C2 * F13")
    context.lookup(ParseTableCells(2, 1)).set("=Q11 / C2")

    val thrownException = intercept[TableEvaluatorError] {
      table.evaluateAllCells(context)
    }

    assert(thrownException.getMessage.contains("Evaluation failed"))
    assert(thrownException.getMessage.contains("F4: Empty cell in arithmetic operation"))
  }

  test("Plain reference to an empty cell") {
    val initialData =Map(
      ParseTableCells(1, 1) ->  Formula(1, 1, expressionParser),
      ParseTableCells(1, 2) -> Empty(1, 2)
    )

    val tableModel = new TableModel(initialData) // Convert to TableModel
    val table = new BaseTable(new FileParser(expressionParser))
    table.initializeRows(tableModel)
    val context = new EvaluationContext(table)

    context.lookup(ParseTableCells(1, 1)).set("=B1") // A1 = B1

    table.evaluateAllCells(context)

    assert(table.getEvaluatedResultAsString(ParseTableCells(1, 1)) == "")
  }

  test("Circular dependency: Formula references itself") {
    val initialData : Map[ParseTableCells, TableEntry]  = Map(
      ParseTableCells(1, 1) ->  Formula(1, 1, expressionParser)
    )

    val tableModel = new TableModel(initialData) // Convert to TableModel
    val table = new BaseTable(new FileParser(expressionParser))
    table.initializeRows(tableModel)
    val context = new EvaluationContext(table)

    context.lookup(ParseTableCells(1, 1)).set("=A1 + 1")


    val thrownException = intercept[TableEvaluatorError] {
      table.evaluateAllCells(context)
    }

    assert(thrownException.getMessage.contains("Circular dependency detected at cell: A1"))
  }

  test("Circular dependency: Formulas referencing each other") {
    val initialData  : Map[ParseTableCells, TableEntry] = Map(
      ParseTableCells(1, 1) ->  Formula(1, 1, expressionParser),
      ParseTableCells(1, 2) ->  Formula(1, 2, expressionParser)
    )

    val tableModel = new TableModel(initialData) // Convert to TableModel
    val table = new BaseTable(new FileParser(expressionParser))
    table.initializeRows(tableModel)
    val context = new EvaluationContext(table)

    context.lookup(ParseTableCells(1, 1)).set("=B1 + 1")
    context.lookup(ParseTableCells(1, 2)).set("=A1 + 1")


    val thrownException = intercept[TableEvaluatorError] {
      table.evaluateAllCells(context)
    }
    assert(thrownException.getMessage.contains("Circular dependency detected at cell: A1"))
    assert(thrownException.getMessage.contains("Circular dependency detected at cell: B1"))
  }

  test("The formula references to a non existing cell in the table"){
    val initialData: Map[ParseTableCells, TableEntry] = Map(
      ParseTableCells(1, 1) -> Formula(1, 1, expressionParser)
    )

    val tableModel = new TableModel(initialData) // Convert to TableModel
    val table = new BaseTable(new FileParser(expressionParser))
    table.initializeRows(tableModel)
    val context = new EvaluationContext(table)

    context.lookup(ParseTableCells(1, 1)).set("=CC99 + 1")


    val thrownException = intercept[IllegalArgumentException] {
      table.evaluateAllCells(context)
    }

    assert(thrownException.getMessage.contains("There is no cell found at position CC99"))
  }

  

  test("Table evaluator with no problems") {
    val initialData = Map(
      ParseTableCells(4, 6) ->  Formula(4, 6, expressionParser),
      ParseTableCells(2, 3) -> Number(2, 3),
      ParseTableCells(11, 17) -> Number(11, 17),
      ParseTableCells(13, 6) -> Empty(13, 6),
      ParseTableCells(2, 1) ->  Formula(2, 1, expressionParser)
    )

    val tableModel = new TableModel(initialData) // Convert to TableModel
    val table = new BaseTable(new FileParser(expressionParser))
    table.initializeRows(tableModel)
    val context = new EvaluationContext(table)

    context.lookup(ParseTableCells(2, 3)).set("18") // C2 = 18
    context.lookup(ParseTableCells(11, 17)).set("4") // Q11 = 4
    // Set formulas
    context.lookup(ParseTableCells(4, 6)).set("=C2 * A2") // F4 = C2 * F13 (F13 is empty)
    context.lookup(ParseTableCells(2, 1)).set("=Q11 / C2") // A2 = Q11 / C2

    table.evaluateAllCells(context)


    assert(table.getEvaluatedResult(ParseTableCells(2, 3)).contains(IntResult(18)))
    assert(table.getEvaluatedResult(ParseTableCells(11, 17)).contains(IntResult(4)))
    assert(table.getEvaluatedResult(ParseTableCells(2, 1)).contains(FloatResult(0.2222222222222222)))
    assert(table.getEvaluatedResult(ParseTableCells(4, 6)).contains(FloatResult(4.0)))
    assert(table.getEvaluatedResult(ParseTableCells(13, 6)).contains(EmptyResult))
  }

  test("Evaluate cells and retrieve evaluated results as strings") {
    val initialData =Map(
      ParseTableCells(1, 1) -> Number(1, 1),
      ParseTableCells(1, 2) ->  Formula(1, 2, expressionParser),
      ParseTableCells(2, 1) ->  Formula(2, 1, expressionParser),
      ParseTableCells(2, 2) -> Empty(2, 2)
    )

    val tableModel = new TableModel(initialData) // Convert to TableModel
    val table = new BaseTable(new FileParser(expressionParser))
    table.initializeRows(tableModel)
    val context = new EvaluationContext(table)



    context.lookup(ParseTableCells(1, 1)).set("10")
    context.lookup(ParseTableCells(1, 2)).set("=A1 + 5")
    context.lookup(ParseTableCells(2, 1)).set("=B1 / 2")

    table.evaluateAllCells(context)
    assert(table.getEvaluatedResultAsString(ParseTableCells(1, 1)) == "10")
    assert(table.getEvaluatedResultAsString(ParseTableCells(1, 2)) == "15")
    assert(table.getEvaluatedResultAsString(ParseTableCells(2, 1)) == "7.5")
    assert(table.getEvaluatedResultAsString(ParseTableCells(2, 2)) == "")
  }
}
