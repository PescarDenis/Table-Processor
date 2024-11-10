package FileReaderTest

import org.scalatest.funsuite.AnyFunSuite
import File_Reader.{CSVReader, CSVSeparator}
import Table.DefinedTabels.BaseTable
import TableParser.{FileParser, ParseTableCells}
import ExpressionParser.ParsingServices.DefaultExpressionParser
import ExpressionParser.ParserLogic.ExpressionBuilder

import scala.io.Source

class TableLoaderTest extends AnyFunSuite {

  private val expressionParser = new DefaultExpressionParser(new ExpressionBuilder)
  private val fileParser = new FileParser(expressionParser)

  test("Load a simple CSV file in the correct format") {
    val source = Source.fromString("1,2,3\n4,5,6")
    val separator = CSVSeparator(",")
    val reader = new CSVReader(source, separator)
    val table = new BaseTable(fileParser)

    // Parse the CSV into the table
    table.parse(reader)

    // Validate table content
    assert(table.getCell(ParseTableCells(1, 1)).get == "1")
    assert(table.getCell(ParseTableCells(1, 2)).get == "2")
    assert(table.getCell(ParseTableCells(1, 3)).get == "3")
    assert(table.getCell(ParseTableCells(2, 1)).get == "4")
    assert(table.getCell(ParseTableCells(2, 2)).get == "5")
    assert(table.getCell(ParseTableCells(2, 3)).get == "6")
  }

  test("Load a CSV with ;; separator and formulas") {
    val source = Source.fromString(" ;1231;=A1+9090*3;9\n;=A2+3/2.0;12;78")
    val separator = CSVSeparator(";")
    val reader = new CSVReader(source, separator)
    val table = new BaseTable(fileParser)

    // Parse the CSV into the table
    table.parse(reader)

    // Validate table content and formulas
    assert(table.getCell(ParseTableCells(1, 1)).get == "")
    assert(table.getCell(ParseTableCells(1, 2)).get == "1231")
    assert(table.getCell(ParseTableCells(1, 3)).get == "MultiplyExpression(AddExpression(CellReferenceExpression(A1),ConstantExpression(IntResult(9090))),ConstantExpression(IntResult(3)))")
    assert(table.getCell(ParseTableCells(1, 4)).get == "9")
    assert(table.getCell(ParseTableCells(2, 1)).get == "")
    assert(table.getCell(ParseTableCells(2, 2)).get == "DivideExpression(AddExpression(CellReferenceExpression(A2),ConstantExpression(IntResult(3))),ConstantExpression(FloatResult(2.0)))")
    assert(table.getCell(ParseTableCells(2, 3)).get == "12")
    assert(table.getCell(ParseTableCells(2, 4)).get == "78")
  }
}
