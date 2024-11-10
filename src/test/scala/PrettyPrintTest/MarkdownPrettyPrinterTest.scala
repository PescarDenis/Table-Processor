package PrettyPrintTest

import org.scalatest.funsuite.AnyFunSuite
import PrettyPrint.*
import Evaluation.EvaluationTypes.*
import Filters.ValueFilter
import Table.DefinedTabels.*
import Table.*
import OutputDestination.*
import File_Reader.CSVSeparator
import TableParser.ParseTableCells

class MarkdownPrettyPrinterTest extends AnyFunSuite {

  val data = Map(
    ParseTableCells(1, 1) -> IntResult(10),
    ParseTableCells(1, 2) -> IntResult(33),
    ParseTableCells(1, 3) -> IntResult(1),
    ParseTableCells(2, 1) -> IntResult(9),
    ParseTableCells(2, 2) -> IntResult(24),
    ParseTableCells(2, 3) -> IntResult(0)
  )

  val table = new MockTableForTests(data)
  PrettyPrinterRegistry.register("md", _ => new MarkdownPrettyPrinter())

  test("Markdown Output with Headers") {
    val mockOutputHandler = new MockOutputHandler()
    val markdownPrettyPrinter = PrettyPrinterRegistry.getPrinter("md", CSVSeparator(",")) // CSVSeparator ignored for Markdown
    val tablePrinter = new TablePrinter(markdownPrettyPrinter)

    tablePrinter.printTable(table, mockOutputHandler, None, None, includeHeaders = true)

    val output = mockOutputHandler.getContent
    println("Markdown Output with Headers:\n" + output)

    val expectedOutput =
        "|  | A | B | C |\n"+
        "| --- | --- | --- | --- |\n"+
        "| 1 | 10 | 33 | 1 |\n"+
        "| 2 | 9 | 24 | 0 |"


    assert(output.trim == expectedOutput.trim, "Markdown output with headers did not match the expected result.")
  }

  test("Markdown Output with NO Headers") {
    val mockOutputHandler = new MockOutputHandler()
    val markdownPrettyPrinter = PrettyPrinterRegistry.getPrinter("md", CSVSeparator(",")) // Separator ignored for Markdown
    val tablePrinter = new TablePrinter(markdownPrettyPrinter)

    tablePrinter.printTable(table, mockOutputHandler, None, None, includeHeaders = false)

    val output = mockOutputHandler.getContent
    println("Markdown Output with NO Headers:\n" + output)

    val expectedOutput =
        "|  |  |  |\n"+
        "| --- | --- | --- |\n"+
        "| 10 | 33 | 1 |\n"+
        "| 9 | 24 | 0 |"


    assert(output.trim == expectedOutput.trim, "Markdown output without headers did not match the expected result.")
  }

  test("Markdown Output with Filter (C = 1)") {
    val mockOutputHandler = new MockOutputHandler()
    val markdownPrettyPrinter = PrettyPrinterRegistry.getPrinter("md", CSVSeparator(","))
    val tablePrinter = new TablePrinter(markdownPrettyPrinter)

    val valueFilter = Some(ValueFilter("C", "==", 1))
    tablePrinter.printTable(table, mockOutputHandler, None, valueFilter, includeHeaders = true)

    val output = mockOutputHandler.getContent
    println("Markdown Output with Filter (C == 1):\n" + output)

    val expectedOutput =
        "|  | A | B | C |\n"+
          "| --- | --- | --- | --- |\n"+
         "| 1 | 10 | 33 | 1 |"

    assert(output.trim == expectedOutput.trim, "Markdown output with filter did not match the expected result.")
  }
}
