package FilterTest
import org.scalatest.funsuite.AnyFunSuite
import Filters.*
import Table.DefinedTabels.*
import Evaluation.EvaluationTypes.*
import TableParser.ParseTableCells

class FilterTest extends AnyFunSuite {
  val data = Map(
    ParseTableCells(1, 1) -> IntResult(10), //A1
    ParseTableCells(1, 2) -> IntResult(22), // B1
    ParseTableCells(2, 1) -> FloatResult(20.5), //A2
    ParseTableCells(2, 2) -> FloatResult(14.5), // B2
    ParseTableCells(3, 1) -> IntResult(30), //A3
    ParseTableCells(3, 2) -> EmptyResult, // B3
    ParseTableCells(4, 1) -> FloatResult(40.2), //A4
    ParseTableCells(4, 2) -> EmptyResult // B4
  )
  val table= new MockTableForTests(data)
  val evaluator = new RowFilterEvaluator()
  test("Filter rows where column B <= 30") {
    val valueFilter = ValueFilter("B", "<=", 30)
    val results = evaluator.evaluateFilter(table,valueFilter)
    val expectedResults = List(true, true, false, false)
    assert(results == expectedResults)
  }

  test("Filter rows where column A != 30") {
    val valueFilter = ValueFilter("A", "!=", 30)
    val results = evaluator.evaluateFilter(table,valueFilter)
    val expectedResults = List(true, true, false, true)
    assert(results == expectedResults)
  }

  test("Filter rows where column B is empty") {
    val emptyCellFilter = EmptyCellFilter("B", isEmpty = true)
    val results = evaluator.evaluateFilter(table,emptyCellFilter)
    val expectedResults = List(false, false, true, true)
    assert(results == expectedResults)
  }
  test("Filter rows with --filter-is-non-empty B and --filter A > 20") {
    // Define filters
    val nonEmptyFilterB = EmptyCellFilter("B", isEmpty = false)
    val valueFilterA = ValueFilter("A", ">", 20.0)

    // Chain filters with logical AND
    val chainedFilter = ChainedFilter(List(nonEmptyFilterB, valueFilterA))
    
    val results = evaluator.evaluateFilter(table,chainedFilter)

    val expectedResults = List(false, true, false, false) // Row 2 matches both conditions
    assert(results == expectedResults)
  }
  test("Filter rows with --filter-is-empty B and --filter A < 25") {
    // Define filters
    val isEmptyFilterB = EmptyCellFilter("B", isEmpty = true)
    val valueFilterA = ValueFilter("A", "<", 25.0)

    // Chain filters with logical AND
    val chainedFilter = ChainedFilter(List(isEmptyFilterB, valueFilterA))

    // Apply filters with evaluator
    val results = evaluator.evaluateFilter(table,chainedFilter)

    val expectedResults = List(false, false, false, false) //none of the rows match both conditions
    assert(results == expectedResults)
  }
  test("Filter rows where column A >= 10.23 and B == 14.5"){
    val valueFilterA = ValueFilter("A", ">=", 10.23)
    val valueFilterB = ValueFilter("B", "==", 14.5)

    // Chain filters with logical AND
    val evaluator= new TableFilterEvaluator(table)
    val chainedFilter = ChainedFilter(List(valueFilterA,valueFilterB))

    // Apply filters with evaluator
    val results = evaluator.evaluateFilter(chainedFilter)

    // Expected output: Only rows meeting both conditions
    val expectedResults = List(false, true, false, false) //none of the rows match both conditions
    assert(results == expectedResults)
  }
  test("Filter but applying a random argument which is not yet supported") {
    val valueFilterA = ValueFilter("A", "<3<#<3<3<3", 10.23)

    val evaluatorWithErrors = new TableFilterEvaluator(table)
    // Apply filters with evaluator
    val exception = intercept[FilterError]{
      evaluatorWithErrors.evaluateFilter(valueFilterA)
    } //the error is that there is an unsupported operator

    assert(exception.getMessage.contains("Unsupported filtering operator: <3<#<3<3<3"))
  }
  test("Apply a filter on a missing collumn")
  {
    val valueFilterA = ValueFilter("KK", "<3<#<3<3<3", 10.23) //it works the same for the empty filter

    val evaluatorWithErrors = new TableFilterEvaluator(table)

    val exception = intercept[FilterError] {
      evaluatorWithErrors.evaluateFilter(valueFilterA)
    }

    assert(exception.getMessage.contains("The given column KK does not exists in the table"))
  }
}
