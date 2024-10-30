package PrettyPrint

import Table.TableInterface
import Table.ParseTableCells

//convert the table data into makrdown formatted string
// include headers it s a flag wether to include headers into the printing
class MarkdownPrettyPrinter(includeHeaders: Boolean) extends PrettyPrinter {
  private def padLeft(s: String, width: Int): String = {
    val padding = " " * (width - s.length)
    s"$padding$s"
  }

  private def padRight(s: String, width: Int): String = {
    val padding = " " * (width - s.length)
    s"$s$padding"
  }

  override def print(table: TableInterface): String = {
    val lastRow = table.lastRow.getOrElse(0)
    val lastColumn = table.lastColumn.getOrElse(0)

    if (lastRow == 0 || lastColumn == 0) return ""

    // Calculate the maximum width of each column
    val colWidths = (1 to lastColumn).map { colIndex =>
      val headerWidth = if (includeHeaders) ParseTableCells.getColName(colIndex).length else 0
      val cellWidths = (1 to lastRow).map { rowIndex =>
        val cellPos = ParseTableCells(rowIndex, colIndex)
        table.getEvaluatedResultAsString(cellPos).length
      }
      (headerWidth +: cellWidths).max
    }

    // Calculate the row number column width
    val rowNumWidth = if (includeHeaders) lastRow.toString.length else 0

    // Create the header row
    val headerRow = {
      val headers = if (includeHeaders) {
        (Seq(padLeft("", rowNumWidth)) ++ (1 to lastColumn).zip(colWidths).map {
          case (colIndex, width) =>
            padLeft(ParseTableCells.getColName(colIndex), width)
        }).mkString("| ", " | ", " |")
      } else {
        colWidths.map { width => padLeft("", width) }.mkString("| ", " | ", " |")
      }
      headers
    }

    // Create the separator row
    val separatorRow = {
      val separators = if (includeHeaders) {
        (Seq("-" * (rowNumWidth + 2)) ++ colWidths.map { width =>
          "-" * (width + 2)
        }).mkString("|", "|", "|")
      } else {
        colWidths.map { width => "-" * (width + 2) }.mkString("|", "|", "|")
      }
      separators
    }

    // Create data rows
    val dataRows = (1 to lastRow).map { rowIndex =>
      val rowCells = (1 to lastColumn).zip(colWidths).map {
        case (colIndex, width) =>
          val cellValue = table.getEvaluatedResultAsString(ParseTableCells(rowIndex, colIndex))
          padRight(cellValue, width)
      }
      if (includeHeaders) {
        val rowNumber = padLeft(rowIndex.toString, rowNumWidth)
        (Seq(rowNumber) ++ rowCells).mkString("| ", " | ", " |")
      } else {
        rowCells.mkString("| ", " | ", " |")
      }
    }

    // Combine all parts
    val outputLines = Seq(headerRow, separatorRow) ++ dataRows

    outputLines.mkString("\n")
  }
}
