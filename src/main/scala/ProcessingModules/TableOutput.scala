package ProcessingModules

import CLIInterface.CLIConfig
import File_Reader.CSVSeparator
import PrettyPrint.{PrettyPrinterRegistry, TablePrinter}
import OutputDestination.{FileOutputHandler, StdoutOutputHandler, OutputHandler}
import Table.TableModel

class TableOutput(config: CLIConfig, rangedModel: TableModel[String], printerRegistry: PrettyPrinterRegistry) {

  def output(): Unit = {
    val separator = CSVSeparator(config.outputSeparator)
    val prettyPrinter = printerRegistry.getPrinter(config.outputFormat, separator)

    // Prepare all output handlers based on the configuration
    val outputHandlers = getOutputHandlers(config)
    
    val tablePrinter = new TablePrinter(prettyPrinter)

    // Print the table using all specified output handlers
    outputHandlers.foreach { handler =>
      tablePrinter.printTable(rangedModel, handler, config.headers)
    }
  }
//helper method to collect both stdout and output file handlers
//in case we specify the output file, the table will also be printed to the stdout as it is always true by default
  protected def getOutputHandlers(config: CLIConfig): List[OutputHandler] = {
    val handlers = List.newBuilder[OutputHandler]

    if (config.outputToStdout) {
      handlers += new StdoutOutputHandler()
    }

    config.outputFile.foreach { filePath =>
      handlers += new FileOutputHandler(filePath)
    }

    handlers.result()
  }
}
