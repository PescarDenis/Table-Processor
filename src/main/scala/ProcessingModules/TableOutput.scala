package ProcessingModules

import CLIInterface.CLIConfig
import File_Reader.CSVSeparator
import PrettyPrint.{PrettyPrinterFactory, TablePrinter}
import OutputDestination.*
import Table.DefinedTabels.BaseTable
import Table.ParseTableCells
import Filters.*

class TableOutput(config: CLIConfig, table: BaseTable) {

  def output(): Unit = {
    val separator = CSVSeparator(config.outputSeparator)
    val printer = PrettyPrinterFactory.getPrinter(config.outputFormat, separator, config.headers)

    val outputHandler = config.outputFile match {
      case Some(filePath) => new FileOutputHandler(filePath)
      case None if config.outputToStdout => new StdoutOutputHandler
      case _ =>
        println("No output destination specified.")
        return
    }

    val printerService = new TablePrinter(printer)
    val range = config.range.map { case (start, end) =>
      ParseTableCells.parse(start).get -> ParseTableCells.parse(end).get
    }
    val filter = config.filters.map(ChainedFilter.apply)

    printerService.printTable(table, outputHandler, range, filter, config.headers)
  }
}
