package PrettyPrint
import Table.TableInterface
import Filters.TableFilter
import OutputDestination.OutputHandler
import TableParser.ParseTableCells

//Classs that prints the table 
class TablePrinter(prettyPrinter: PrettyPrinter) {
  def printTable(
                  table: TableInterface,
                  outputHandler: OutputHandler,
                  range: Option[(ParseTableCells, ParseTableCells)],
                  filter: Option[TableFilter],
                  includeHeaders: Boolean
                ): Unit = {
    val content = prettyPrinter.print(table, range, filter, includeHeaders)
    outputHandler.write(content)
  }
}