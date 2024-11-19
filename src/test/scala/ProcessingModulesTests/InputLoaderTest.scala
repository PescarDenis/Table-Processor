package ProcessingModulesTests

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfter
import ProcessingModules.InputLoader
import CLIInterface.CLIConfig
import File_Reader.CSVSeparator
import Table.DefinedTabels.BaseTable
import TableParser.{FileParser, ParseTableCells}
import ExpressionParser.ParsingServices.DefaultExpressionParser
import ExpressionParser.ParserLogic.ExpressionBuilder

import java.io.{File, PrintWriter}
import scala.util.Using

class InputLoaderTest extends AnyFunSuite with BeforeAndAfter {

  private val testFile = new File("test.csv")

  before {
    // Create a mock CSV file
    val writer = new PrintWriter(testFile)
    writer.println("1,2,3")
    writer.println("4,5*2,=B2+C1/3.21")
    writer.println(",8,9")
    writer.close()
  }

  after {
    // Clean up test file
    if (testFile.exists()) {
      testFile.delete()
    }
  }

  test("Load table from CSV file with default separator") {
    val config = CLIConfig(
      inputFile = Some(testFile.getPath),
      inputSeparator = ","
    )

    val parser = new FileParser(new DefaultExpressionParser(new ExpressionBuilder[Any]))
    val loader = new InputLoader(config, parser)

    val table = loader.loadTable()
    val cellA1 = table.getCell(ParseTableCells(1, 1)).get
    val cellB1 = table.getCell(ParseTableCells(1,2)).get
    val cellC1 = table.getCell(ParseTableCells(1,3)).get
    val cellA2 = table.getCell(ParseTableCells(2,1)).get
    val cellB2 = table.getCell(ParseTableCells(2, 2)).get
    val cellC2 = table.getCell(ParseTableCells(2, 3)).get
    val cellA3 = table.getCell(ParseTableCells(3, 1)).get
    val cellB3 = table.getCell(ParseTableCells(3, 2)).get
    val cellC3 = table.getCell(ParseTableCells(3, 3)).get

    assert(cellA1 == "1")
    assert(cellB1 == "2")
    assert(cellC1 == "3")
    assert(cellA2 == "4")
    assert(cellB2 == "MultiplyExpression(ConstantExpression(IntResult(5)),ConstantExpression(IntResult(2)))")
    assert(cellC2 == "DivideExpression(AddExpression(CellReferenceExpression(B2),CellReferenceExpression(C1)),ConstantExpression(FloatResult(3.21)))")
    assert(cellA3 == "")
    assert(cellB3 == "8")
    assert(cellC3 == "9")
  }

  test("Load table from CSV file with custom separator") {
    val customSeparatorFile = new File("test-separator.csv")
    Using(new PrintWriter(customSeparatorFile)) { writer =>
      writer.println("1;2;3")
      writer.println("4;5*2;=B2+C1/3.21")
      writer.println(";8;9")
    }

    val config = CLIConfig(
      inputFile = Some(customSeparatorFile.getPath),
      inputSeparator = ";"
    )

    val parser = new FileParser(new DefaultExpressionParser(new ExpressionBuilder[Any]))
    val loader = new InputLoader(config, parser)

    val table = loader.loadTable()
    val cellA1 = table.getCell(ParseTableCells(1, 1)).get
    val cellB1 = table.getCell(ParseTableCells(1, 2)).get
    val cellC1 = table.getCell(ParseTableCells(1, 3)).get
    val cellA2 = table.getCell(ParseTableCells(2, 1)).get
    val cellB2 = table.getCell(ParseTableCells(2, 2)).get
    val cellC2 = table.getCell(ParseTableCells(2, 3)).get
    val cellA3 = table.getCell(ParseTableCells(3, 1)).get
    val cellB3 = table.getCell(ParseTableCells(3, 2)).get
    val cellC3 = table.getCell(ParseTableCells(3, 3)).get

    assert(cellA1 == "1")
    assert(cellB1 == "2")
    assert(cellC1 == "3")
    assert(cellA2 == "4")
    assert(cellB2 == "MultiplyExpression(ConstantExpression(IntResult(5)),ConstantExpression(IntResult(2)))")
    assert(cellC2 == "DivideExpression(AddExpression(CellReferenceExpression(B2),CellReferenceExpression(C1)),ConstantExpression(FloatResult(3.21)))")
    assert(cellA3 == "")
    assert(cellB3 == "8")
    assert(cellC3 == "9")

    customSeparatorFile.delete()
  }

  test("Load table throws exception for missing file") {
    val config = CLIConfig(
      inputFile = Some("nonexistent.csv"),
      inputSeparator = ","
    )

    val parser = new FileParser(new DefaultExpressionParser(new ExpressionBuilder[Any]))
    val loader = new InputLoader(config, parser)

    val thrown = intercept[Exception] {
      loader.loadTable()
    }
    assert(thrown.getMessage.contains("The system cannot find the file specified"))
  }

  test("Load a table with defalut :: separator but introduce a ; somewhere"){
    val customSeparatorFile = new File("test-separator.csv")
    Using(new PrintWriter(customSeparatorFile)) { writer =>
      writer.println("1::   2:: 3")
      writer.println("4; 5::   =B2+C1/3.21")
    }

    val config = CLIConfig(
      inputFile = Some(customSeparatorFile.getPath),
      inputSeparator = "::"
    )

    val parser = new FileParser(new DefaultExpressionParser(new ExpressionBuilder[Any]))
    val loader = new InputLoader(config, parser)
    
    val thrown = intercept[IllegalArgumentException] {
      loader.loadTable()
    }
    //it will just blow up, because the whole thing is treated as a non valid cell
    assert(thrown.getMessage.contains("Invalid cell content at (2,1): '4; 5'. Expected a number, formula, or empty cell."))

    customSeparatorFile.delete()
  }
}
