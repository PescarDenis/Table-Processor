package TableTest

import org.scalatest.funsuite.AnyFunSuite
import Table.{Table, ParseTableCells}
import File_Reader.MockCSVReader
import ExpressionAST.{AddExpression, ConstantExpression,CellReferenceExpression}
import Evaluation.EvaluationTypes.IntResult

class TableTest extends AnyFunSuite {

  test("BasicTable with mock CSVReader including formulas and cell names") {
    // Define some mock CSV data, including formulas and numbers
    val mockData = List(
      List("", "1", "=A1 + 22"),        // Row 1: B1 is a number, C1 is a formula
      List("231231", "3", ""," "), // Row 2: Multiple numbers and an empty cell
      List("4", "2", "")               // Row 3: A3 is empty, B3 and A3 have numbers
    )

    // Create a mock CSVReader with the predefined data
    val mockCSVReader = new MockCSVReader(mockData)

    // Create a BasicTable and parse the mock CSV data
    val table = new Table().parse(mockCSVReader)

    // Test last row and column
    assert(table.lastRow.contains(3))
    assert(table.lastColumn.contains(4))

    // Test individual cells with actual cell names
    assert(table.getCell(ParseTableCells.parse("B1").get).get == "1")      // B1 = 1
    assert(table.getCell(ParseTableCells.parse("A2").get).get == "231231") // A2 = 231231
    assert(table.getCell(ParseTableCells.parse("A3").get).get == "4")      // A3 = 4
    assert(table.getCell(ParseTableCells.parse("B3").get).get == "2")      // B3 = 2

    // Test formula cells using cell names
    assert(table.getCell(ParseTableCells.parse("C1").get).get == AddExpression(CellReferenceExpression( ParseTableCells.parse("A1").get),ConstantExpression(IntResult(22))).toString) // C1 = "=A1 + 22"

    // Test non-empty positions with actual cell names
    val nonEmptyPos = table.nonEmptyPositions
    assert(nonEmptyPos.exists(p => p == ParseTableCells.parse("B1").get)) // Cell B1 should not be empty
    assert(nonEmptyPos.exists(p => p == ParseTableCells.parse("A2").get)) // Cell A2 should not be empty
    assert(nonEmptyPos.exists(p => p == ParseTableCells.parse("A3").get)) // Cell A3 should not be empty
  }
}
