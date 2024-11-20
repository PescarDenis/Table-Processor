package ProcessingModulesTests

import CLIInterface.CLIConfig
import Evaluation.EvaluationResult
import Evaluation.EvaluationTypes._
import org.scalatest.funsuite.AnyFunSuite
import Range.RangeError
import Table.TableModel
import TableParser.ParseTableCells
import ProcessingModules.RangeSelector

class RangeSelectorTest extends AnyFunSuite {

  // Mock table data for testing
  val data: Map[ParseTableCells, EvaluationResult[?]] = Map(
    ParseTableCells(1, 1) -> IntResult(10),   // A1
    ParseTableCells(1, 2) -> IntResult(22),   // B1
    ParseTableCells(2, 1) -> FloatResult(20.5), // A2
    ParseTableCells(2, 2) -> FloatResult(14.5), // B2
    ParseTableCells(3, 1) -> IntResult(30),   // A3
    ParseTableCells(3, 2) -> EmptyResult,     // B3
    ParseTableCells(4, 1) -> FloatResult(40.2), // A4
    ParseTableCells(4, 2) -> EmptyResult      // B4
  )

  val filteredModel = new TableModel(data)

  test("Valid range selection from A1 to B3") {
    val config = CLIConfig(
      inputFile = None, // Not used in this test
      filters = List.empty,
      range = Some("A1","B3"))
    val rangeSelector = new RangeSelector(config, filteredModel)

    val resultModel = rangeSelector.selectRange()

    val expectedData = Map(
      ParseTableCells(1, 1) -> "10",   // A1
      ParseTableCells(1, 2) -> "22",   // B1
      ParseTableCells(2, 1) -> "20.5", // A2
      ParseTableCells(2, 2) -> "14.5", // B2
      ParseTableCells(3, 1) -> "30",   // A3
      ParseTableCells(3, 2) -> ""      // B3
    )

    assert(resultModel.toMap == expectedData)
  }

  test("Invalid range selection with missing start cell") {
    val config = CLIConfig(
      inputFile = None, // Not used in this test
      filters = List.empty,
      range = Some("Z1", "B3"))
    val rangeSelector = new RangeSelector(config, filteredModel)

    val thrown = intercept[RangeError] {
      rangeSelector.selectRange()
    }

    assert(thrown.getMessage.contains("Start cell Z1 does not exist in the table"))
  }

  test("Invalid range selection with missing end cell") {
    val config = CLIConfig(
      inputFile = None, // Not used in this test
      filters = List.empty,
      range = Some("A1","Z5"))
    val rangeSelector = new RangeSelector(config, filteredModel)

    val thrown = intercept[RangeError] {
      rangeSelector.selectRange()
    }

    assert(thrown.getMessage.contains("End cell Z5 does not exist in the table"))
  }

  test("Default range selection for all non-empty cells") {
    val config = CLIConfig(
      inputFile = None, // Not used in this test
      filters = List.empty,
      range = None)
    val rangeSelector = new RangeSelector(config, filteredModel)

    val resultModel = rangeSelector.selectRange()

    val expectedData = Map(
      ParseTableCells(1, 1) -> "10",   // A1
      ParseTableCells(1, 2) -> "22",   // B1
      ParseTableCells(2, 1) -> "20.5", // A2
      ParseTableCells(2, 2) -> "14.5", // B2
      ParseTableCells(3, 1) -> "30",   // A3
      ParseTableCells(3, 2) -> "",     // B3
      ParseTableCells(4, 1) -> "40.2", // A4
      ParseTableCells(4, 2) -> ""      // B4
    )

    assert(resultModel.toMap == expectedData)
  }
  test("Invalid range selection with an invalid start cell reference") {
    val config = CLIConfig(
      inputFile = None, // Not used in this test
      filters = List.empty,
      range = Some("hello", "Z5"))
    val rangeSelector = new RangeSelector(config, filteredModel)

    val thrown = intercept[RangeError] {
      rangeSelector.selectRange()
    }

    assert(thrown.getMessage.contains("Invalid start cell reference: hello"))
  }
  test("Invalid range selection with an invalid end cell reference") {
    val config = CLIConfig(
      inputFile = None, // Not used in this test
      filters = List.empty,
      range = Some("B3", "goodbye"))
    val rangeSelector = new RangeSelector(config, filteredModel)

    val thrown = intercept[RangeError] {
      rangeSelector.selectRange()
    }

    assert(thrown.getMessage.contains("Invalid end cell reference: goodbye"))
  }
  test("Range with only empty cells"){
    val emptyData : Map[ParseTableCells, EvaluationResult[?]] = Map(
      ParseTableCells(5, 5) -> EmptyResult,
      ParseTableCells(6, 6) -> EmptyResult
    )

    val emptyModel = new TableModel(emptyData)

    val config = CLIConfig(
      inputFile = None, // Not used in this test
      filters = List.empty,
      range = Some("E5", "F6"))

    val rangeSelector = new RangeSelector(config, emptyModel)
    val resultModel = rangeSelector.selectRange()

    val expectedData = Map(
      ParseTableCells(5, 5) -> "",
      ParseTableCells(6, 6) -> "")
    assert(resultModel.toMap == expectedData)
  }

}
