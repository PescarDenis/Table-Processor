package FilterTest
import org.scalatest.funsuite.AnyFunSuite
import Filters.*
import Table.DefinedTabels._
import Table.ParseTableCells
import Evaluation.EvaluationTypes._
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
  val evaluator = new TableFilterEvaluator(table)
  test("Filter rows where column B <= 30 with mixed types") {
    val valueFilter = ValueFilter("B", "<=", 30)
    val results = evaluator.evaluateFilter(valueFilter)
    val expectedResults = List(true, true, false, false)
    assert(results == expectedResults)
  }

  test("Filter rows where column B is empty") {
    val emptyCellFilter = EmptyCellFilter("B", isEmpty = true)
    val results = evaluator.evaluateFilter(emptyCellFilter)
    val expectedResults = List(false, false, true, true)
    assert(results == expectedResults)
  }
  test("Filter rows with --filter-is-non-empty B and --filter A > 20") {
    // Define filters
    val nonEmptyFilterB = EmptyCellFilter("B", isEmpty = false)
    val valueFilterA = ValueFilter("A", ">", 20.0)

    // Chain filters with logical AND
    val chainedFilter = ChainedFilter(List(nonEmptyFilterB, valueFilterA))

    // Apply filters with evaluator
    val evaluator = new TableFilterEvaluator(table)
    val results = evaluator.evaluateFilter(chainedFilter)

    // Expected output: Only rows meeting both conditions
    val expectedResults = List(false, true, false, false) // Row 2 matches both conditions
    assert(results == expectedResults)
  }
}
