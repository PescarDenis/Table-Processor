package PrettyPrintTest

import org.scalatest.funsuite.AnyFunSuite
import PrettyPrint._
import Evaluation.EvaluationTypes._
import Table.DefinedTabels._
import File_Reader.CSVSeparator
import Filters._
import Table._
import OutputDestination._
import TableParser.ParseTableCells

class CSVPrinterTest extends AnyFunSuite {

  val data = Map(
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

  val table: TableInterface = new MockTableForTests(data)

  // Register PrettyPrinters in the registry
  PrettyPrinterRegistry.register("csv", sep => new CSVPrettyPrinter(sep))

  test("Normal output with no range/filter, including headers") {
    val mockOutputHandler = new MockOutputHandler()
    val prettyPrinter = PrettyPrinterRegistry.getPrinter("csv", CSVSeparator(","))

    val tablePrinter = new TablePrinter(prettyPrinter)

    tablePrinter.printTable(table, mockOutputHandler, None, None, includeHeaders = true)

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
    val prettyPrinter = PrettyPrinterRegistry.getPrinter("csv", CSVSeparator(","))

    val tablePrinter = new TablePrinter(prettyPrinter)

    val range = Some((ParseTableCells(2, 2), ParseTableCells(4, 4)))
    tablePrinter.printTable(table, mockOutputHandler, range, None, includeHeaders = true)

    val output = mockOutputHandler.getContent
    val expectedOutput =
      ", B, C, D\n" +
        "2, 20, 200, 2000\n" +
        "3, 32.21, 300, 3000\n" +
        "4, 40, 400, 4000"

    assert(output == expectedOutput)
  }

  test("Range with filter") {
    val mockOutputHandler = new MockOutputHandler()
    val prettyPrinter = PrettyPrinterRegistry.getPrinter("csv", CSVSeparator(","))

    val tablePrinter = new TablePrinter(prettyPrinter)

    val range = Some((ParseTableCells(2, 2), ParseTableCells(4, 4)))
    val filter = Some(EmptyCellFilter("E", isEmpty = false)) // Include rows where column E is non-empty
    tablePrinter.printTable(table, mockOutputHandler, range, filter, includeHeaders = false)

    val output = mockOutputHandler.getContent
    val expectedOutput =
      "32.21, 300, 3000\n" +
        "40, 400, 4000"


    assert(output == expectedOutput)
  }
}
