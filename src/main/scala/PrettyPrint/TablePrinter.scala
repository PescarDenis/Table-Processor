package PrettyPrint
import Table.TableInterfaces
import Filters.TableFilter
import Table.ParseTableCells
import OutputDestination.OutputHandler
//Classs that prints the table
//parametrize it over prettyprinter
class TablePrinter[T <: PrettyPrinter](printer : T) {
  def printTable(
                  table: TableInterface,
                  outputHandler: OutputHandler,
                  range: Option[(ParseTableCells, ParseTableCells)],
                  filter: Option[TableFilter],
                  includeHeaders: Boolean
                ): Unit = {
    val content = printer.print(table, range, filter, includeHeaders)
    outputHandler.write(content)
  }
}