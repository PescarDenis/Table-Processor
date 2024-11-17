package PrettyPrintTest

import Evaluation.EvaluationResult
import org.scalatest.funsuite.AnyFunSuite
import PrettyPrint.*
import Evaluation.EvaluationTypes.*
import Table.DefinedTabels.*
import File_Reader.CSVSeparator
import Filters.*
import Table.*
import OutputDestination.*
import Range.TableRangeEvaluator
import TableParser.ParseTableCells

class CSVPrinterTest extends AnyFunSuite {

  val data: Map[ParseTableCells, EvaluationResult[?]] = Map(
    ParseTableCells(1, 1) -> IntResult(1),
    ParseTableCells(1, 2) -> FloatResult(10.909),
    ParseTableCells(1, 3) -> IntResult(100),
    ParseTableCells(1, 4) -> IntResult(1000),
    ParseTableCells(1, 5) -> IntResult(0),
    ParseTableCells(2, 1) -> IntResult(2),
    ParseTableCells(2, 2) -> IntResult(20),
    ParseTableCells(2, 3) -> IntResult(200),
    ParseTableCells(2, 4) -> IntResult(2000),
    ParseTableCells(2, 5) -> EmptyResult,
    ParseTableCells(3, 1) -> IntResult(3),
    ParseTableCells(3, 2) -> FloatResult(32.21),
    ParseTableCells(3, 3) -> IntResult(300),
    ParseTableCells(3, 4) -> IntResult(3000),
    ParseTableCells(3, 5) -> FloatResult(1.23),
    ParseTableCells(4, 1) -> IntResult(4),
    ParseTableCells(4, 2) -> IntResult(40),
    ParseTableCells(4, 3) -> IntResult(400),
    ParseTableCells(4, 4) -> IntResult(4000),
    ParseTableCells(4, 5) -> IntResult(0),
    ParseTableCells(5, 1) -> IntResult(5),
    ParseTableCells(5, 2) -> FloatResult(52.12),
    ParseTableCells(5, 3) -> IntResult(500),
    ParseTableCells(5, 4) -> EmptyResult,
    ParseTableCells(5, 5) -> IntResult(1)
  )

  def convertToStringModel(table: TableInterface): TableModel[String] = {
    val stringifiedResults = table.nonEmptyPositions.map { pos =>
      pos -> table.getEvaluatedResultAsString(pos)
    }.toMap
    new TableModel(stringifiedResults)
  }

  val table: TableInterface = new MockTableForTests(data)

  // Register PrettyPrinters in the registry
  val printerRegistry = new PrettyPrinterRegistry()
  printerRegistry.register("csv", sep => new CSVPrettyPrinter(sep))

  test("Normal output with no range/filter, including headers") {
    val mockOutputHandler = new MockOutputHandler()
    val prettyPrinter = printerRegistry.getPrinter("csv", CSVSeparator(","))

    val tablePrinter = new TablePrinter(prettyPrinter)
    val stringifiedModel = convertToStringModel(table) // Convert table to TableModel[String]

    tablePrinter.printTable(stringifiedModel, mockOutputHandler, includeHeaders = true)

    val output = mockOutputHandler.getContent
    val expectedOutput =
      ", A, B, C, D, E\n" +
        "1, 1, 10.909, 100, 1000, 0\n" +
        "2, 2, 20, 200, 2000, \n" +
        "3, 3, 32.21, 300, 3000, 1.23\n" +
        "4, 4, 40, 400, 4000, 0\n" +
        "5, 5, 52.12, 500, , 1"

    assert(output == expectedOutput)
  }

  test("Range with headers") {
    val mockOutputHandler = new MockOutputHandler()
    val prettyPrinter = printerRegistry.getPrinter("csv", CSVSeparator(","))

    val rangeEvaluator = new TableRangeEvaluator(new TableModel(data))
    val rangedModel = rangeEvaluator.getResultsInRange(ParseTableCells(2, 2), ParseTableCells(4, 4))

    val stringifiedModel = new TableModel(rangedModel.iterator.map {
      case (pos, result) => pos -> table.getEvaluatedResultAsString(pos)
    }.toMap)

    val tablePrinter = new TablePrinter(prettyPrinter)
    tablePrinter.printTable(stringifiedModel, mockOutputHandler, includeHeaders = true)

    val output = mockOutputHandler.getContent
    val expectedOutput =
      ", B, C, D\n" +
        "2, 20, 200, 2000\n" +
        "3, 32.21, 300, 3000\n" +
        "4, 40, 400, 4000"

    assert(output == expectedOutput)
  }

  test("Filter and range selection") {
    val mockOutputHandler = new MockOutputHandler()
    val prettyPrinter = printerRegistry.getPrinter("csv", CSVSeparator(","))

    val filter = EmptyCellFilter("E", isEmpty = false)
    val evaluator = new Filters.TableFilterEvaluator(table)
    val filteredRows = evaluator.evaluateFilter(filter).zipWithIndex.collect {
      case (true, idx) => idx + 1
    }.toSet

    val filteredData = data.filter { case (cell, _) => filteredRows.contains(cell.row) }
    val rangeEvaluator = new TableRangeEvaluator(new TableModel(filteredData))
    val rangedModel = rangeEvaluator.getResultsInRange(ParseTableCells(2, 2), ParseTableCells(4, 4))

    val stringifiedModel = new TableModel(rangedModel.iterator.map {
      case (pos, result) => pos -> table.getEvaluatedResultAsString(pos)
    }.toMap)

    val tablePrinter = new TablePrinter(prettyPrinter)
    tablePrinter.printTable(stringifiedModel, mockOutputHandler, includeHeaders = false)

    val output = mockOutputHandler.getContent
    val expectedOutput =
      "32.21, 300, 3000\n" +
        "40, 400, 4000"

    assert(output == expectedOutput)
  }
}
