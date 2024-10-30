package PrettyPrint

import Table.TableInterface
import Table.ParseTableCells
import File_Reader.CSVSeparator

class CSVPrettyPrinter(separator: CSVSeparator, includeHeaders: Boolean) extends PrettyPrinter {
  override def print(table: TableInterface): String = {
    val lastRow = table.lastRow.getOrElse(0)
    val lastColumn = table.lastColumn.getOrElse(0)
    val sep = separator.CellSeparator // Use the separator from CSVSeparator

    // Create the header row if headers are included
    val headers = if (includeHeaders) {
      val rowNumberHeader = if (includeHeaders) "" else ""
      val columnHeaders = (1 to lastColumn)
        .map(ParseTableCells.getColName)
        .mkString(sep)
      s"$rowNumberHeader$sep$columnHeaders\n"
    } else {
      ""
    }

    // Create the rows with optional row numbers
    val rows = (1 to lastRow).map { rowIndex =>
      val rowValues = (1 to lastColumn).map { colIndex =>
        val cellPos = ParseTableCells(rowIndex, colIndex)
        table.getEvaluatedResultAsString(cellPos)
      }.mkString(sep)

      if (includeHeaders) {
        val rowNumber = rowIndex.toString
        s"$rowNumber$sep$rowValues"
      } else {
        rowValues
      }
    }

    headers + rows.mkString("\n")
  }
}
