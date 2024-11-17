package PrettyPrint

import Table.TableModel
import TableParser.ParseTableCells

class MarkdownPrettyPrinter extends BaseTablePrettyPrinter {
  override protected def buildHeaders(
                                       cols: Seq[Int],
                                       includeHeaders: Boolean
                                     ): Option[String] = {
    if (!includeHeaders) {
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
    rows.iterator.toSeq.groupBy(_._1.row).toSeq.sortBy(_._1).map {
      case (rowIdx, rowCells) =>
        val rowData = rowCells.sortBy(_._1.col).map(_._2)
        if (includeRowNumbers) (Seq(rowIdx.toString) ++ rowData).mkString("| ", " | ", " |")
        else rowData.mkString("| ", " | ", " |")
    }
  }
}