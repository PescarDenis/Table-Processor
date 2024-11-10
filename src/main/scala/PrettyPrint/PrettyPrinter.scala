package PrettyPrint
import Table.TableInterface
import Filters.TableFilter
import TableParser.ParseTableCells

///base interface that allows the user to print the table in different formats
trait PrettyPrinter {
  def print(
             table: TableInterface,
             range: Option[(ParseTableCells, ParseTableCells)],
             filter: Option[TableFilter] ,
             includeHeaders: Boolean
           ): String
}
