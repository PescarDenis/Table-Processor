package TableTest

import Evaluation.EvaluationTypes.IntResult
import org.scalatest.funsuite.AnyFunSuite
import File_Reader.MockCSVReader
import ExpressionAST.{AddExpression, CellReferenceExpression, ConstantExpression}
import ExpressionParser.ParserLogic.ExpressionBuilder.ExpressionBuilder
import Table.DefinedTabels.BaseTable
import TableParser.{FileParser, ParseTableCells}
import ExpressionParser.ParsingServices.DefaultExpressionParser

class TableParserTest extends AnyFunSuite {

  // Create an instance of ExpressionParser for testing
  private val expressionParser = new DefaultExpressionParser(new ExpressionBuilder)
  private val fileParser = new FileParser(expressionParser)

  test("BaseTable with mock CSVReader including formulas and cell names") {
    // Define some mock CSV data, including formulas and numbers
    val mockData = List(
      List("", "1", "=A1 + 22"),   // Row 1: B1 is a number, C1 is a formula
      List("231231", "3", "", ""), // Row 2: Multiple numbers and empty cells
      List("4", "2", "")           // Row 3: A3 is a number, B3 is a number
    )

    // Create a mock CSVReader with the predefined data
    val mockCSVReader = new MockCSVReader(mockData)

    // Create a BaseTable and parse the mock CSV data
    val table = new BaseTable(fileParser)
    table.parse(mockCSVReader)

    // Test last row and column
    assert(table.lastRow.contains(3))
    assert(table.lastColumn.contains(4))

    // Test individual cells with actual cell names
    assert(table.getCell(ParseTableCells.parse("B1").get).get == "1")      // B1 = 1
    assert(table.getCell(ParseTableCells.parse("A2").get).get == "231231") // A2 = 231231
    assert(table.getCell(ParseTableCells.parse("A3").get).get == "4")      // A3 = 4
    assert(table.getCell(ParseTableCells.parse("B3").get).get == "2")      // B3 = 2
    assert(table.getCell(ParseTableCells.parse("A1").get).get == "")
    assert(table.getCell(ParseTableCells.parse("C2").get).get == "")
    assert(table.getCell(ParseTableCells.parse("D2").get).get == "")
    assert(table.getCell(ParseTableCells.parse("C3").get).get == "")
    // Test formula cells using cell names
    val expectedExpression = AddExpression(
      CellReferenceExpression(ParseTableCells.parse("A1").get),
      ConstantExpression(IntResult(22))
    ).toString
    assert(table.getCell(ParseTableCells.parse("C1").get).get == expectedExpression) // C1 = "=A1 + 22"

  }

  test("Invalid cell found in the parsing process"){
    val mockData = List(
      List("abab","2","3","=A1+22"),
      List("","2+3","","ccc")
    )

    val mockCSVReader = new MockCSVReader(mockData)

    // Create a BaseTable and parse the mock CSV data
    val table = new BaseTable(fileParser)
    val thrownException = intercept[NumberFormatException] {
      table.parse(mockCSVReader)
    }
    // As it founds a cell that is not valid, it just alters the program flows at the first occurrence of it
    assert(thrownException.getMessage.contains("Invalid cell content at (1,1): 'abab'. Expected an integer positive number, formula, or empty cell."))

  }

  
}
