package ProcessingModulesTests

import Evaluation.EvaluationTypes.{EmptyResult, FloatResult, IntResult}
import org.scalatest.funsuite.AnyFunSuite
import CLIInterface.CLIConfig
import Filters.{FilterError, ValueFilter}
import ProcessingModules.Filtering
import Table.DefinedTabels.MockTableForTests
import TableParser.ParseTableCells

class FilteringTest extends AnyFunSuite {
  // Pre-evaluated mock data
  val mockData = Map(
    ParseTableCells(1, 1) -> IntResult(10), // A1
    ParseTableCells(1, 2) -> IntResult(22), // B1
    ParseTableCells(2, 1) -> FloatResult(20.5), // A2
    ParseTableCells(2, 2) -> FloatResult(14.5), // B2
    ParseTableCells(3, 1) -> IntResult(30), // A3
    ParseTableCells(3, 2) -> EmptyResult // B3
  )

  val mockTable = new MockTableForTests(mockData)

  test("Apply multiple filters to a pre-evaluated mock table") {

    // Define CLIConfig with filters
    val config = CLIConfig(
      inputFile = None, // Not used in this test
      inputSeparator = ",",
      filters = List(
        ValueFilter("A", ">", 15),
        ValueFilter("B", "<=", 22)
      )
    )

    val filtering = new Filtering(config, mockTable)
    val filteredModel = filtering.applyFilters()

    // Expected filtered results
    val expectedData = Map(
      ParseTableCells(2, 1) -> FloatResult(20.5),
      ParseTableCells(2, 2) -> FloatResult(14.5)  //row 2 passes
    )

    assert(filteredModel.toMap == expectedData)
  }

  test("Apply filter with no matching rows") {

    val config = CLIConfig(
      inputFile = None,
      inputSeparator = "/.,", //Does not matter as we have no input file
      filters = List(ValueFilter("A", ">", 100)) //No cell in column A > 100
    )

    val filtering = new Filtering(config, mockTable)
    val filteredModel = filtering.applyFilters()

    // Expect no rows to match
    assert(filteredModel.toMap.isEmpty)
  }

  test("Filter on non-empty cells") {

    val config = CLIConfig(
      inputFile = None,
      inputSeparator = "::::::",
      filters = List(Filters.EmptyCellFilter("B", isEmpty = false))
    )

    val filtering = new Filtering(config, mockTable)
    val filteredModel = filtering.applyFilters()

    val expectedData = Map( ParseTableCells(1, 1) -> IntResult(10),  //row 1 and 2 pass
      ParseTableCells(1, 2) -> IntResult(22),
      ParseTableCells(2, 1) -> FloatResult(20.5),
      ParseTableCells(2, 2) -> FloatResult(14.5))
    assert(filteredModel.toMap == expectedData)
  }
  test("Apply filter with an unknown operator") {

    val config = CLIConfig(
      inputFile = None,
      inputSeparator = "/.,", //Does not matter as we have no input file
      filters = List(ValueFilter("B", "__DD", 0.12))
    )

    val filtering = new Filtering(config, mockTable)
    val filteredModel = intercept[FilterError] {
      filtering.applyFilters()
    }

    assert(filteredModel.getMessage.contains("Unsupported filtering operator: __DD")) //No row matches and the error is thrown in the console
  }
  test("Apply filter with a non existing column in the table") {

    val config = CLIConfig(
      inputFile = None,
      inputSeparator = "/.,", //Does not matter as we have no input file
      filters = List(ValueFilter("DDD", "==", 0.12))
    )

    val filtering = new Filtering(config, mockTable)
    val filteredModel = intercept[FilterError] {
      filtering.applyFilters()
    }


    assert(filteredModel.getMessage.contains("The given column DDD does not exists in the table")) //No row matches and the error is thrown in the console
  }
}
