  package ProcessingModules

  import CLIInterface.CLIConfig
  import File_Reader.CSVSeparator
  import PrettyPrint.{PrettyPrinterRegistry, TablePrinter}
  import Table.DefinedTabels.BaseTable
  import Filters._
  import OutputDestination._
  import TableParser.ParseTableCells

  // Outputs the table based on various settings
  class TableOutput(config: CLIConfig, table: BaseTable) {

    def output(): Unit = {
      // Configure PrettyPrinter based on CLIConfig
      val separator = CSVSeparator(config.outputSeparator)
      val outputFormat = config.outputFormat
      val includeHeaders = config.headers

      // Get PrettyPrinter from Registry
      val prettyPrinter = PrettyPrinterRegistry.getPrinter(outputFormat, separator)

      // Define OutputHandler based on CLIConfig
      val outputHandler = config.outputFile match {
        case Some(filePath) => new FileOutputHandler(filePath)
        case None if config.outputToStdout => new StdoutOutputHandler
        case _ =>
          println("No output destination specified.")
          return
      }

      // Instantiate TablePrinter with PrettyPrinter and use the outputHandler
      val tablePrinter = new TablePrinter(prettyPrinter)

      // Parse range and configure ChainedFilter for multiple filters
      val rangeOption = config.range.map { case (start, end) =>
        val fromCell = ParseTableCells.parse(start).getOrElse(
          throw new IllegalArgumentException(s"Invalid start cell reference: $start")
        )
        val toCell = ParseTableCells.parse(end).getOrElse(
          throw new IllegalArgumentException(s"Invalid end cell reference: $end")
        )
        (fromCell, toCell)
      }
      val filterOption: Option[TableFilter] = if (config.filters.nonEmpty) Some(ChainedFilter(config.filters)) else None

      // Print and output the table using TablePrinter and OutputHandler
      tablePrinter.printTable(table, outputHandler, rangeOption, filterOption, includeHeaders)
    }
  }
