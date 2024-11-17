package PrettyPrint

import Table.TableModel
import File_Reader.CSVSeparator
import TableParser.ParseTableCells

class CSVPrettyPrinter(separator: CSVSeparator) extends BaseTablePrettyPrinter {
  override protected def buildHeaders(
                                       cols: Seq[Int],
                                       includeHeaders: Boolean
                                     ): Option[String] = {
    if (!includeHeaders) None
    else Some((Seq("") ++ cols.map(ParseTableCells.getColName)).mkString(separator.CellSeparator + " "))
  }

  override protected def buildRows(
                                    rows: TableModel[String],
                                    includeRowNumbers: Boolean
                                  ): Seq[String] = {
    rows.iterator.toSeq.groupBy(_._1.row).toSeq.sortBy(_._1).map {
      case (rowIdx, rowCells) =>
        val rowData = rowCells.sortBy(_._1.col).map(_._2)
        if (includeRowNumbers) (Seq(rowIdx.toString) ++ rowData).mkString(separator.CellSeparator + " ")
        else rowData.mkString(separator.CellSeparator + " ")
    }
  }
}
