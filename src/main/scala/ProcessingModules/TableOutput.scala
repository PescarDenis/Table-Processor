package ProcessingModules

import CLIInterface.CLIConfig
import File_Reader.CSVSeparator
import PrettyPrint.{PrettyPrinterRegistry, TablePrinter}
import OutputDestination.{FileOutputHandler, StdoutOutputHandler}
import Table.DefinedTabels.BaseTable
import TableParser.ParseTableCells
import Filters.{ChainedFilter, TableFilter}

class TableOutput(config: CLIConfig, table: BaseTable) {

  def output(): Unit = {
    val separator = CSVSeparator(config.outputSeparator)
    val prettyPrinter = PrettyPrinterRegistry.getPrinter(config.outputFormat, separator)

    val outputHandler = config.outputFile match {
      case Some(filePath) => new FileOutputHandler(filePath)
      case None if config.outputToStdout => new StdoutOutputHandler
      case _ =>
        println("No output destination specified.")
        return
    }

    val range = config.range.flatMap { case (start, end) =>
      for {
        fromCell <- ParseTableCells.parse(start)
        toCell <- ParseTableCells.parse(end)
      } yield (fromCell, toCell)
    }

    val filter: Option[TableFilter] =
      if (config.filters.nonEmpty) Some(ChainedFilter(config.filters)) else None

    val tablePrinter = new TablePrinter(prettyPrinter)
    tablePrinter.printTable(table, outputHandler, range, filter, config.headers)
  }
}
