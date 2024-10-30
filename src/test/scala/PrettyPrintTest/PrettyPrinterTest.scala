package PrettyPrintTest

import org.scalatest.funsuite.AnyFunSuite
import PrettyPrint._
import Table.ParseTableCells
import Evaluation.EvaluationTypes._
import Table.DefinedTabels._
import File_Reader.CSVSeparator
class PrettyPrinterTest extends AnyFunSuite {

  test("CSVPrettyPrinter with basic table data, comma separator") {
    // Arrange
    val data = Map(
      ParseTableCells(1, 1) -> IntResult(10),
      ParseTableCells(1, 2) -> IntResult(33),
      ParseTableCells(2, 1) -> IntResult(9),
      ParseTableCells(2, 2) -> IntResult(24)
    )
    val table = new MockTableForTests(data)
    val csvPrinter = PrettyPrinterFactory.getPrinter("csv", CSVSeparator(","), includeHeaders = false)

    // Act
    val output = csvPrinter.print(table)

    println("CSV Table Output with , :\n" + output)
    // Assert
    val expectedOutput = "10,33\n9,24"
    assert(output == expectedOutput)
  }

  test("CSVPrettyPrinter with different data and semicolon separator") {
    // Arrange
    val data = Map(
      ParseTableCells(1, 1) -> IntResult(5),
      ParseTableCells(1, 2) -> FloatResult(15.5),
      ParseTableCells(2, 1) -> IntResult(20),
      ParseTableCells(2, 2) -> IntResult(32)
    )
    val table = new MockTableForTests(data)
    val csvPrinter = new CSVPrettyPrinter(CSVSeparator(";"), includeHeaders = true)

    // Act
    val output = csvPrinter.print(table)

    // Assert
    val expectedOutput =
        ";A;B\n" +
        "1;5;15.5\n" +
        "2;20;32"
    println("CSV Table Output with ; :\n" + output)
    assert(output == expectedOutput)
  }

  test("MarkdownPrettyPrinter with three columns and headers") {
    // Arrange
    val data = Map(
      ParseTableCells(1, 1) -> IntResult(10),
      ParseTableCells(1, 2) -> IntResult(33333333),
      ParseTableCells(1, 3) -> IntResult(1),
      ParseTableCells(2, 1) -> FloatResult(9.1),
      ParseTableCells(2, 2) -> EmptyResult,
      ParseTableCells(2, 3) -> IntResult(0)
    )
    val table = new MockTableForTests(data) // Mock class implementing TableInterface
    val mdPrinter = new MarkdownPrettyPrinter(includeHeaders = true)

    // Act
    val output = mdPrinter.print(table)
    println("Markdown Table Output:\n" + output)
    // Define expected Markdown output
    val expectedOutput =
        "|   |   A |        B | C |\n" +
        "|---|-----|----------|---|\n" +
        "| 1 | 10  | 33333333 | 1 |\n" +
        "| 2 | 9.1 |          | 0 |"

    // Assert
    assert(output == expectedOutput)
  }

  test("MarkdownPrettyPrinter with three columns and no headers") {
    // Arrange
    val data = Map(
      ParseTableCells(1, 1) -> IntResult(10),
      ParseTableCells(1, 2) -> IntResult(33),
      ParseTableCells(1, 3) -> IntResult(1),
      ParseTableCells(2, 1) -> IntResult(9),
      ParseTableCells(2, 2) -> IntResult(24),
      ParseTableCells(2, 3) -> IntResult(0)
    )
    val table = new MockTableForTests(data) // Mock class implementing TableInterface
    val mdPrinter = new MarkdownPrettyPrinter(includeHeaders = false)

    // Act
    val output = mdPrinter.print(table)
    println("Markdown Table Output:\n" + output)
    // Define expected Markdown output
    val expectedOutput =
        "|    |    |   |\n" +
        "|----|----|---|\n" +
        "| 10 | 33 | 1 |\n" +
        "| 9  | 24 | 0 |"

    // Assert
    assert(output == expectedOutput)
  }
}