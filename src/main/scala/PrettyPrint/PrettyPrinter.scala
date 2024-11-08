package PrettyPrint
import Table.TableInterfaces
import Table.ParseTableCells
import Filters.TableFilter
///base interface that allows the user to print the table in different formats
trait PrettyPrinter {
  def print(
             table: TableInterface,
             range: Option[(ParseTableCells, ParseTableCells)],
             filter: Option[TableFilter] ,
             includeHeaders: Boolean
           ): String
}
