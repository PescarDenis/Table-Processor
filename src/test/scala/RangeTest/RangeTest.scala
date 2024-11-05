package RangeTest

import org.scalatest.funsuite.AnyFunSuite
import Table.ParseTableCells
import Table.DefinedTabels.TableRange
import Evaluation.EvaluationTypes._
import Table.DefinedTabels.MockTableForTests

class RangeTest extends AnyFunSuite {
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
  val mockTable = new MockTableForTests(data)
  val tableRange = new TableRange(mockTable)
  test("Range selection from A1 to B3") {

    // Define the expected result map for the range A1 to B3
    val expectedRange = Map(
      ParseTableCells(1, 1) -> IntResult(10), // A1
      ParseTableCells(1, 2) -> IntResult(22), // B1
      ParseTableCells(2, 1) -> FloatResult(20.5), // A2
      ParseTableCells(2, 2) -> FloatResult(14.5), // B2
      ParseTableCells(3, 1) -> IntResult(30), // A3
      ParseTableCells(3, 2) -> EmptyResult // B3 (empty cell)
    )

    // Get the actual range from A1 to B3
    val range1 = tableRange.getRange(ParseTableCells(1, 1), ParseTableCells(3, 2))
    val range2 = tableRange.getRange(ParseTableCells(3, 2), ParseTableCells(1, 1))
    val range3 = tableRange.getRange(ParseTableCells(1, 2), ParseTableCells(3, 1))
    val range4 = tableRange.getRange(ParseTableCells(3, 1), ParseTableCells(1, 2))

    tableRange.printRange(range2)

    assert(range1 == expectedRange)
    assert(range2 == expectedRange)
    assert(range3 == expectedRange)
    assert(range4 == expectedRange)
  }

  test("Range selection from A2 to B4") {
    val expectedRange = Map(
      ParseTableCells(2, 1) -> FloatResult(20.5), // A2
      ParseTableCells(2, 2) -> FloatResult(14.5), // B2
      ParseTableCells(3, 1) -> IntResult(30), // A3
      ParseTableCells(3, 2) -> EmptyResult, // B3
      ParseTableCells(4, 1) -> FloatResult(40.2), // A4
      ParseTableCells(4, 2) -> EmptyResult // B4
    )

    val range1 = tableRange.getRange(ParseTableCells(2, 1), ParseTableCells(4, 2))
    val range2 = tableRange.getRange(ParseTableCells(4, 1), ParseTableCells(2, 2))
    val range3 = tableRange.getRange(ParseTableCells(4, 2), ParseTableCells(2, 1))
    val range4 = tableRange.getRange(ParseTableCells(2, 2), ParseTableCells(4, 1))

    tableRange.printRange(range1)

    assert(range1 == expectedRange)
    assert(range2 == expectedRange)
    assert(range3 == expectedRange)
    assert(range4 == expectedRange)
  }

  test("Single row selection across all columns") {
    val expectedRange = Map(
      ParseTableCells(3, 1) -> IntResult(30), // A3
      ParseTableCells(3, 2) -> EmptyResult // B3
    )

    val range1 = tableRange.getRange(ParseTableCells(3, 1), ParseTableCells(3, 2))
    val range2 = tableRange.getRange(ParseTableCells(3, 2), ParseTableCells(3, 1))


    tableRange.printRange(range1)

    assert(range1 == expectedRange)
    assert(range2 == expectedRange)
  }
}

