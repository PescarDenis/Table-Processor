package ProcessingModules

import CLIInterface.CLIConfig
import File_Reader.CSVSeparator
import PrettyPrint.{PrettyPrinterRegistry, TablePrinter}
import OutputDestination.{FileOutputHandler, StdoutOutputHandler}
import Table.TableModel

class TableOutput(config: CLIConfig, rangedModel: TableModel[String]) {

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

    val tablePrinter = new TablePrinter(prettyPrinter)
    tablePrinter.printTable(rangedModel, outputHandler, config.headers)
  }
}


