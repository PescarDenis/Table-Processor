package PrettyPrint

import Table.TableModel
import TableParser.ParseTableCells

abstract class BaseTablePrettyPrinter extends PrettyPrinter {

  // Abstract methods for formatting headers and rows
  protected def buildHeaders(
                              cols: Seq[Int],
                              includeHeaders: Boolean
                            ): Option[String]

  protected def buildRows(
                           rows: TableModel[String],
                           includeRowNumbers: Boolean
                         ): Seq[String]

  // Print the content passed as a `TableModel[String]`
  override def print(
                      tableData: TableModel[String],
                      includeHeaders: Boolean
                    ): String = {
    val cols = tableData.nonEmptyPositions.map(_.col).toSeq.distinct.sorted
    val headers = buildHeaders(cols, includeHeaders)
    val dataRows = buildRows(tableData, includeRowNumbers = includeHeaders)

    (headers.toSeq ++ dataRows).mkString("\n")
  }
}
