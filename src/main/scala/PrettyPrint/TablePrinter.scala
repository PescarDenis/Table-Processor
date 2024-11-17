package PrettyPrint
import Table.TableModel
import OutputDestination.OutputHandler

class TablePrinter(prettyPrinter: PrettyPrinter) {

  def printTable(
                  tableModel: TableModel[String],
                  outputHandler: OutputHandler,
                  includeHeaders: Boolean
                ): Unit = {
    val content = prettyPrinter.print(tableModel, includeHeaders)
    outputHandler.write(content)
  }
}



