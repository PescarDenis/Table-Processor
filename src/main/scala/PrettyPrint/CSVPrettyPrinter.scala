package PrettyPrint

import Table.{TableInterface, TableModel}
import Filters.TableFilter
import File_Reader.CSVSeparator
import TableParser.ParseTableCells

class CSVPrettyPrinter(separator: CSVSeparator) extends BaseTablePrettyPrinter {

  override protected def buildHeaders(
                                       cols: Seq[Int],
                                       includeHeaders: Boolean
                                     ): Option[String] = {
    if (!includeHeaders) None
    else {
      val colHeaders = cols.map(ParseTableCells.getColName)
      Some((Seq("") ++ colHeaders).mkString(separator.CellSeparator + " "))
    }
  }

  override protected def buildRows(
                                    rows: TableModel[String],
                                    includeRowNumbers: Boolean
                                  ): Seq[String] = {
    rows.iterator.toSeq.groupBy{case (pos, _) => pos.row }.toSeq.sortBy(_._1).map { case (rowIdx, rowCells) =>
      val rowData = rowCells.sortBy(_._1.col).map(_._2)
      if (includeRowNumbers) {
        (Seq(rowIdx.toString) ++ rowData).mkString(separator.CellSeparator + " ")
      } else {
        rowData.mkString(separator.CellSeparator + " ")
      }
    }
  }

  override def print(
                      table: TableInterface,
                      range: Option[(ParseTableCells, ParseTableCells)],
                      filter: Option[TableFilter],
                      includeHeaders: Boolean
                    ): String = {
    val effectiveRange = getEffectiveRange(table, range)
    val rows = getFilteredRows(table, effectiveRange, filter)

    val headers = buildHeaders(effectiveRange._1.col to effectiveRange._2.col, includeHeaders)
    val dataRows = buildRows(rows, includeRowNumbers = includeHeaders)

    (headers.toSeq ++ dataRows).mkString("\n")
  }
}
