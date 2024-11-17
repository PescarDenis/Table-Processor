package PrettyPrint
import Table.{TableInterface, TableModel}
import Filters.TableFilter
import TableParser.ParseTableCells

///base interface that allows the user to print the table in different formats
trait PrettyPrinter {
  def print(
             tableData: TableModel[String],
             includeHeaders: Boolean
           ): String
}
