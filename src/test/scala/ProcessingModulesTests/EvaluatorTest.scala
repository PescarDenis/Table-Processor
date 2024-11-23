package ProcessingModulesTests
import Evaluation.EvaluationTypes.{EmptyResult, FloatResult, IntResult}
import ExpressionParser.ParserLogic.ExpressionBuilder.ExpressionBuilder
import ExpressionParser.ParsingServices.DefaultExpressionParser
import ProcessingModules.Evaluator
import Table.DefinedTabels.BaseTable
import Table.TableEntries.{Empty, Formula, Number}
import Table.TableModel
import TableParser.{FileParser, ParseTableCells}
import org.scalatest.funsuite.AnyFunSuite

//essentially the evaluator is just a wrapper for the TableEvaluator, so there is no need to test
//all of the edge cases as we did for the table evaluator

class EvaluatorTest extends AnyFunSuite{
  private val expressionParser = new DefaultExpressionParser(new ExpressionBuilder)
  test("Evaluate a table with no errors") {
    val initialData = Map(
      ParseTableCells(1, 1) -> Number(1, 1), // A1 = 5
      ParseTableCells(1, 2) -> Number(1, 2), // B1 = 3
      ParseTableCells(2, 1) ->Empty(1, 2), //A2
      ParseTableCells(2, 2) -> Formula(1, 3, expressionParser) // B2
    )

    val tableModel = new TableModel(initialData)
    val table = new BaseTable(new FileParser(expressionParser))
    table.initializeRows(tableModel)

    table.getCell(ParseTableCells(1, 1)).set("5") // A1 = 5
    table.getCell(ParseTableCells(1, 2)).set("3") // B1 = 3
    table.getCell(ParseTableCells(2, 1)).set("") // B1 = 3
    table.getCell(ParseTableCells(2, 2)).set("=A1 * B1 /2") // C1 = A1 * B1

    val evaluator = new Evaluator(table)
    evaluator.evaluateAll()

    assert(table.getEvaluatedResult(ParseTableCells(1, 1)).contains(IntResult(5)))
    assert(table.getEvaluatedResult(ParseTableCells(1, 2)).contains(IntResult(3)))
    assert(table.getEvaluatedResult(ParseTableCells(2, 1)).contains(EmptyResult))
    assert(table.getEvaluatedResult(ParseTableCells(2, 2)).contains(FloatResult(7.5)))
  }
}
