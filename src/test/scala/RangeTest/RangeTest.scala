package RangeTest

import Evaluation.EvaluationResult
import org.scalatest.funsuite.AnyFunSuite
import Evaluation.EvaluationTypes.*
import Range.TableRangeEvaluator
import Table.DefinedTabels.MockTableForTests
import Table.TableModel
import TableParser.ParseTableCells

class RangeTest extends AnyFunSuite {

  // Mock data to simulate table with evaluated results
  val data: Map[ParseTableCells, EvaluationResult[_]] = Map(
    ParseTableCells(1, 1) -> IntResult(10),   // A1
    ParseTableCells(1, 2) -> IntResult(22),   // B1
    ParseTableCells(2, 1) -> FloatResult(20.5), // A2
    ParseTableCells(2, 2) -> FloatResult(14.5), // B2
    ParseTableCells(3, 1) -> IntResult(30),   // A3
    ParseTableCells(3, 2) -> EmptyResult,     // B3
    ParseTableCells(4, 1) -> FloatResult(40.2), // A4
    ParseTableCells(4, 2) -> EmptyResult      // B4
  )

  // Mock table implementation
  val tableModel = new TableModel(data)
  val rangeEvaluator = new TableRangeEvaluator(tableModel)

  test("Range selection from A1 to B3") {
    val expectedRange = Map(
      ParseTableCells(1, 1) -> IntResult(10), // A1
      ParseTableCells(1, 2) -> IntResult(22), // B1
      ParseTableCells(2, 1) -> FloatResult(20.5), // A2
      ParseTableCells(2, 2) -> FloatResult(14.5), // B2
      ParseTableCells(3, 1) -> IntResult(30), // A3
      ParseTableCells(3, 2) -> EmptyResult // B3
    )

    val rangeResults = rangeEvaluator.getResultsInRange(ParseTableCells(1, 1), ParseTableCells(3, 2))

    assert(rangeResults.toMap == expectedRange)
  }
  test("Range selection from A2 to B4") {
    val expectedRange = Map(
      ParseTableCells(2, 1) -> FloatResult(20.5), // A2
      ParseTableCells(2, 2) -> FloatResult(14.5), // B2
      ParseTableCells(3, 1) -> IntResult(30),   // A3
      ParseTableCells(3, 2) -> EmptyResult,     // B3
      ParseTableCells(4, 1) -> FloatResult(40.2), // A4
      ParseTableCells(4, 2) -> EmptyResult      // B4
    )

    val rangeResults = rangeEvaluator.getResultsInRange(ParseTableCells(2, 1), ParseTableCells(4, 2))
    assert(rangeResults.toMap == expectedRange)
  }

  test("Single row selection across all columns") {
    val expectedRange = Map(
      ParseTableCells(3, 1) -> IntResult(30),   // A3
      ParseTableCells(3, 2) -> EmptyResult      // B3
    )

    val rangeResults = rangeEvaluator.getResultsInRange(ParseTableCells(3, 1), ParseTableCells(3, 2))
    assert(rangeResults.toMap == expectedRange)
  }

  test("Default range selection") {
    val expectedRange = Map(
      ParseTableCells(1, 1) -> IntResult(10),   // A1
      ParseTableCells(1, 2) -> IntResult(22),   // B1
      ParseTableCells(2, 1) -> FloatResult(20.5), // A2
      ParseTableCells(2, 2) -> FloatResult(14.5), // B2
      ParseTableCells(3, 1) -> IntResult(30),   // A3
      ParseTableCells(3, 2) -> EmptyResult,     // B3
      ParseTableCells(4, 1) -> FloatResult(40.2), //A4
      ParseTableCells(4, 2) -> EmptyResult //B4
    )

    val defaultRangeResults = rangeEvaluator.getDefaultRangeResults
    assert(defaultRangeResults.toMap == expectedRange)
  }
  test("Default range strips empty rows and columns") {
    val data: Map[ParseTableCells, EvaluationResult[Any]] = Map(
      ParseTableCells(1, 1) -> IntResult(10), // A1
      ParseTableCells(1, 2) -> IntResult(22), // B1
      ParseTableCells(2, 1) -> FloatResult(20.5), // A2
      ParseTableCells(2, 2) -> FloatResult(14.5), // B2
      ParseTableCells(3, 1) -> IntResult(30), // A3
      ParseTableCells(3, 2) -> EmptyResult, // B3
      ParseTableCells(4, 1) -> EmptyResult, // A4
      ParseTableCells(4, 2) -> EmptyResult // B4
    )

    val tableModel = new TableModel(data)

    val rangeEvaluator = new TableRangeEvaluator(tableModel)

    val defaultRangeResults = rangeEvaluator.getDefaultRangeResults

    val expectedResults = Map(
      ParseTableCells(1, 1) -> IntResult(10), // A1
      ParseTableCells(1, 2) -> IntResult(22), // B1
      ParseTableCells(2, 1) -> FloatResult(20.5), // A2
      ParseTableCells(2, 2) -> FloatResult(14.5), // B2
      ParseTableCells(3, 1) -> IntResult(30), // A3
      ParseTableCells(3, 2) -> EmptyResult, // B3
      //A4 B4 the row is completly empty so we strip it from the range
    )

    assert(defaultRangeResults.toMap == expectedResults)
  }
}