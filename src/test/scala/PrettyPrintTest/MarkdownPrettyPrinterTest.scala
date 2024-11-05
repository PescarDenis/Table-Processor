package PrettyPrintTest

import org.scalatest.funsuite.AnyFunSuite
import PrettyPrint._
import Table.ParseTableCells
import Evaluation.EvaluationTypes._
import Filters.ValueFilter
import Table.DefinedTabels._
import Table._
import OutputDestination._
import File_Reader.CSVSeparator
class MarkdownPrettyPrinterTest extends  AnyFunSuite {

  val data = Map(
    ParseTableCells(1, 1) -> IntResult(10),
    ParseTableCells(1, 2) -> IntResult(33),
    ParseTableCells(1, 3) -> IntResult(1),
    ParseTableCells(2, 1) -> FloatResult(9.231321),
    ParseTableCells(2, 2) -> IntResult(24),
    ParseTableCells(2, 3) -> IntResult(0)
  )

  val table = new MockTableForTests(data)
  PrettyPrinterRegistry.register("csv", sep => new CSVPrettyPrinter(sep))
  PrettyPrinterRegistry.register("md", _ => new MarkdownPrettyPrinter())

  test("Markdown Output with Headers") {
    val mockOutputHandler = new MockOutputHandler()

    //for test purposes, the CSV separator does not matter when we use MD
    val markdownPrettyPrinter = PrettyPrinterRegistry.getPrinter("md", CSVSeparator(","))
    val tablePrinter = new TablePrinter(markdownPrettyPrinter)

    // Print the table with headers
    tablePrinter.printTable(table, mockOutputHandler, None, None, includeHeaders = true)

    // Capture and print the output
    val output = mockOutputHandler.getContent
    println("Markdown Output with Headers:\n" + output)

    // Expected output in Markdown format with headers
    val expectedOutput =
         "|   |        A |  B | C |\n"+
         "|---|----------|----|---|\n"+
         "| 1 | 10       | 33 | 1 |\n"+
         "| 2 | 9.231321 | 24 | 0 |"

    // Assert that the output matches the expected Markdown format
    assert(output == expectedOutput, "Markdown output with headers did not match the expected result.")
  }

  test("Markdown Output with NO Headers") {
    val mockOutputHandler = new MockOutputHandler()

    //for test purposes, the CSV separator does not matter when we use MD
    val markdownPrettyPrinter = PrettyPrinterRegistry.getPrinter("md", CSVSeparator(";;;"))
    val tablePrinter = new TablePrinter(markdownPrettyPrinter)

    // Print the table with headers
    tablePrinter.printTable(table, mockOutputHandler, None, None, includeHeaders = false)

    // Capture and print the output
    val output = mockOutputHandler.getContent
    println("Markdown Output with NO Headers:\n" + output)

    // Expected output in Markdown format with headers
    val expectedOutput =
        "|          |    |   |\n"+
        "|----------|----|---|\n"+
        "| 10       | 33 | 1 |\n"+
        "| 9.231321 | 24 | 0 |"

    // Assert that the output matches the expected Markdown format
    assert(output == expectedOutput, "Markdown output with headers did not match the expected result.")
  }

  test("Markdown Output with Filter (C = 1)") {
    val mockOutputHandler = new MockOutputHandler()

    //for test purposes, the CSV separator does not matter when we use MD
    val markdownPrettyPrinter = PrettyPrinterRegistry.getPrinter("md", CSVSeparator(","))
    val tablePrinter = new TablePrinter(markdownPrettyPrinter)

    // Print the table with headers and filter
    val valueFilter = Some(ValueFilter("C", "==", 1))
    tablePrinter.printTable(table, mockOutputHandler, None, valueFilter, includeHeaders = true)

    // Capture and print the output
    val output = mockOutputHandler.getContent
    println("Markdown Output with Filter (C == 1):\n" + output)

    // Expected output: Only row 1 should be included
    val expectedOutput =
        "|   |  A |  B | C |\n" +
        "|---|----|----|---|\n" +
        "| 1 | 10 | 33 | 1 |"

    // Assert that the output matches the expected Markdown format
    assert(output.trim == expectedOutput.trim, "Markdown output with filter did not match the expected result.")
  }

}
