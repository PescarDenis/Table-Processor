package PrettyPrint
import Table.TableModel

///base interface that allows the user to print the table in different formats
trait PrettyPrinter {
  def print(
             tableData: TableModel[String],
             includeHeaders: Boolean
           ): String
}
