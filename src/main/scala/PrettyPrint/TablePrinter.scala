package PrettyPrint
import Table.TableModel
import OutputDestination.OutputHandler

//Prints the final table where we want to 
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



