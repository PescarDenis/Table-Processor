package PrettyPrintTest

import org.scalatest.funsuite.AnyFunSuite
import PrettyPrint.*
import Evaluation.EvaluationTypes.*
import File_Reader.CSVSeparator
import Filters.*
import Table.DefinedTabels.*
import Table.*
import OutputDestination.*
import TableParser.ParseTableCells
import Range.TableRangeEvaluator

class MarkdownPrettyPrinterTest extends AnyFunSuite {

  val data: Map[ParseTableCells, IntResult] = Map(
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

    val stringifiedModel = new TableModel(data.map {
      case (pos, eval) => pos -> table.getEvaluatedResultAsString(pos)
    })

    tablePrinter.printTable(stringifiedModel, mockOutputHandler, includeHeaders = true)

    val output = mockOutputHandler.getContent
    val expectedOutput =
        "|  | A | B | C |\n" +
        "| --- | --- | --- | --- |\n" +
        "| 1 | 10 | 33 | 1 |\n" +
        "| 2 | 9 | 24 | 0 |"

    assert(output.trim == expectedOutput.trim)
  }

  test("Markdown Output with NO Headers") {
    val mockOutputHandler = new MockOutputHandler()
    val markdownPrettyPrinter = PrettyPrinterRegistry.getPrinter("md", CSVSeparator(",")) // Separator ignored for Markdown
    val tablePrinter = new TablePrinter(markdownPrettyPrinter)

    val stringifiedModel = new TableModel(data.map {
      case (pos, eval) => pos -> table.getEvaluatedResultAsString(pos)
    })

    tablePrinter.printTable(stringifiedModel, mockOutputHandler, includeHeaders = false)

    val output = mockOutputHandler.getContent
    val expectedOutput =
        "|  |  |  |\n"+
        "| --- | --- | --- |\n"+
        "| 10 | 33 | 1 |\n"+
        "| 9 | 24 | 0 |"

    assert(output.trim == expectedOutput.trim)
  }

  test("Markdown Output with Filter (C == 1)") {
    val mockOutputHandler = new MockOutputHandler()
    val markdownPrettyPrinter = PrettyPrinterRegistry.getPrinter("md", CSVSeparator(","))
    val filter = ValueFilter("C", "==", 1)
    val evaluator = new Filters.TableFilterEvaluator(table)
    val filteredRows = evaluator.evaluateFilter(filter).zipWithIndex.collect {
      case (true, idx) => idx + 1
    }.toSet

    val filteredData = data.filter { case (cell, _) => filteredRows.contains(cell.row) }
    val rangeEvaluator = new TableRangeEvaluator(new TableModel(filteredData))
    val rangedModel = rangeEvaluator.getDefaultRangeResults

    val stringifiedModel = new TableModel(filteredData.map {
      case (pos, eval) => pos -> table.getEvaluatedResultAsString(pos)
    })

    val tablePrinter = new TablePrinter(markdownPrettyPrinter)
    tablePrinter.printTable(stringifiedModel, mockOutputHandler, includeHeaders = true)

    val output = mockOutputHandler.getContent
    val expectedOutput =
        "|  | A | B | C |\n" +
        "| --- | --- | --- | --- |\n" +
        "| 1 | 10 | 33 | 1 |"
    assert(output.trim == expectedOutput.trim)
  }
}
