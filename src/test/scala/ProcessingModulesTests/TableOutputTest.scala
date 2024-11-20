package ProcessingModulesTests

import org.scalatest.funsuite.AnyFunSuite
import ProcessingModules.TableOutput
import PrettyPrint.{CSVPrettyPrinter, MarkdownPrettyPrinter, PrettyPrinterRegistry}
import OutputDestination.{MockOutputHandler, OutputHandler}
import Table.TableModel
import CLIInterface.CLIConfig
import TableParser.ParseTableCells

class TableOutputTest extends AnyFunSuite {

  val sampleData: Map[ParseTableCells, String] = Map(
    ParseTableCells(1, 1) -> "10",
    ParseTableCells(1, 2) -> "22",
    ParseTableCells(2, 1) -> "20.5",
    ParseTableCells(2, 2) -> "14.5",
    ParseTableCells(3, 1) -> "30",
    ParseTableCells(3, 2) -> ""
  )

  val tableModel = new TableModel(sampleData)

  val printerRegistry = new PrettyPrinterRegistry()
  printerRegistry.register("csv", sep => new CSVPrettyPrinter(sep))
  printerRegistry.register("md", _ => new MarkdownPrettyPrinter)
  test("Output to MockOutputHandler with headers") {
    val mockOutputHandler = new MockOutputHandler()

    val config = CLIConfig(
      outputFormat = "csv",
      outputSeparator = ",",
      headers = true,
      outputToStdout = true,
      outputFile = None
    )

    // Subclass to override handlers with a mock
    val tableOutput = new TableOutput(config, tableModel, printerRegistry) {
      override protected def getOutputHandlers(config: CLIConfig): List[OutputHandler] = List(mockOutputHandler)
    }

    tableOutput.output()

    val expectedOutput =
        ", A, B\n"+
        "1, 10, 22\n"+
        "2, 20.5, 14.5\n"+
        "3, 30,"

    assert(mockOutputHandler.getContent == expectedOutput)
  }
  test("Output to MockOutputHandler without headers") {
    val mockOutputHandler = new MockOutputHandler()

    val config = CLIConfig(
      outputFormat = "csv",
      outputSeparator = ";",
      headers = false,
      outputToStdout = true,
      outputFile = None
    )

    val tableOutput = new TableOutput(config, tableModel, printerRegistry) {
      override protected def getOutputHandlers(config: CLIConfig): List[OutputHandler] = List(mockOutputHandler)
    }

    tableOutput.output()

    val expectedOutput =
        "10; 22\n" +
        "20.5; 14.5\n" +
        "30;"

    assert(mockOutputHandler.getContent == expectedOutput)
  }
  test("Normal markdown to stdout and file "){
    val mockOutputHandler = new MockOutputHandler()

    val config = CLIConfig(
      outputFormat = "md",
      outputSeparator = ",",
      headers = true,
      outputToStdout = true,
      outputFile =Some("output_file.md")

    )

    val tableOutput = new TableOutput(config, tableModel, printerRegistry) {
      override protected def getOutputHandlers(config: CLIConfig): List[OutputHandler] = List(mockOutputHandler)
    }

    tableOutput.output()

    val expectedOutput =
        "|  | A | B |\n"+
        "| --- | --- | --- |\n"+
        "| 1 | 10 | 22 |\n"+
        "| 2 | 20.5 | 14.5 |\n"+
        "| 3 | 30 |  |"

    assert(mockOutputHandler.getContent == expectedOutput)

  }

  test("Unknown output format") {
    val mockOutputHandler = new MockOutputHandler()

    val config = CLIConfig(
      outputFormat = "xml",
      outputSeparator = ";",
      headers = false,
      outputToStdout = true,
      outputFile = None
    )
    val tableOutput = new TableOutput(config, tableModel, printerRegistry) {
      override protected def getOutputHandlers(config: CLIConfig): List[OutputHandler] = List(mockOutputHandler)
    }
    val output = intercept[IllegalArgumentException]{
      tableOutput.output()
    }
    assert(output.getMessage.contains("Unknown format: xml"))
  }
}
