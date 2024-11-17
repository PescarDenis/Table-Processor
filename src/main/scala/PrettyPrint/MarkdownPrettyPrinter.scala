package PrettyPrint

import Table.{TableInterface, TableModel}
import Filters.TableFilter
import TableParser.ParseTableCells

class MarkdownPrettyPrinter extends BaseTablePrettyPrinter {

  override protected def buildHeaders(
                                       cols: Seq[Int],
                                       includeHeaders: Boolean
                                     ): Option[String] = {
    if (!includeHeaders) {
      // Create a blank header row followed by a separator row
      val blankHeaderRow = cols.map(_ => "").mkString("| ", " | ", " |")
      val separatorRow = cols.map(_ => "---").mkString("| ", " | ", " |")
      Some(s"$blankHeaderRow\n$separatorRow")
    } else {
      val colHeaders = cols.map(ParseTableCells.getColName)
      val headerRow = (Seq("") ++ colHeaders).mkString("| ", " | ", " |")
      val separatorRow = (Seq("---") ++ colHeaders.map(_ => "---")).mkString("| ", " | ", " |")
      Some(s"$headerRow\n$separatorRow")
    }
  }

  override protected def buildRows(
                                    rows: TableModel[String],
                                    includeRowNumbers: Boolean
                                  ): Seq[String] = {
    rows.iterator.toSeq.groupBy{case (pos, _) => pos.row }.toSeq.sortBy(_._1).map { case (rowIdx, rowCells) =>
      val rowData = rowCells.sortBy(_._1.col).map(_._2)
      if (includeRowNumbers) {
        (Seq(rowIdx.toString) ++ rowData).mkString("| ", " | ", " |")
      } else {
        rowData.mkString("| ", " | ", " |")
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

    // Combine headers/separator and rows
    (headers.toSeq ++ dataRows).mkString("\n")
  }
}
