package PrettyPrint

import Evaluation.EvaluationTypes.EvaluationResult
import Table.{ParseTableCells, TableInterfaces}
import Table.DefinedTabels.{TableFilterEvaluator, TableRange}
import Filters.TableFilter
import Table.TableInterfaces.EvaluatedTableInterface

//Markdown printer class -> knows how to print the table in the MD format
class MarkdownPrettyPrinter extends PrettyPrinter {
  //the padding methods are used for aligning the text
  //pad a string to the left
  private def padLeft(s: String, width: Int): String = {
    val padding = " " * (width - s.length)
    s"$padding$s"//left for evaluated values
  }
  //pad a string to the right
  private def padRight(s: String, width: Int): String = {
    val padding = " " * (width - s.length)
    s"$s$padding"//right for headers
  }

  override def print(
                      table: EvaluatedTableInterface[EvaluationResult[_]],
                      range: Option[(ParseTableCells, ParseTableCells)],
                      filter: Option[TableFilter],
                      includeHeaders: Boolean
                    ): String = {

    val tableRange = new TableRange(table)
    val tableFilterEvaluator = new TableFilterEvaluator(table)

    //determine effective range: full table if None
    val effectiveRange = range.getOrElse {
      val maxRow = table.lastRow.getOrElse(1)
      val maxCol = table.lastColumn.getOrElse(1)
      (ParseTableCells(1, 1), ParseTableCells(maxRow, maxCol))
    }

    //get cells within the specified or default range
    val cellsInRange = tableRange.getRange(effectiveRange._1, effectiveRange._2)

    //apply filter if provided, otherwise include all rows
    val filteredRows = filter match {
      case Some(f) =>
        val filterResults = tableFilterEvaluator.evaluateFilter(f)
        val matchingRowIndices = filterResults.zipWithIndex.collect {
          case (true, idx) => idx + 1 //row indices are 1-based
        }.toSet
        cellsInRange.groupBy(_._1.row).filter { case (rowIndex, _) => matchingRowIndices.contains(rowIndex) }
      case None => cellsInRange.groupBy(_._1.row)
    }
    if (filteredRows.isEmpty) return "" //early exit if no data rows are available

    //determine columns to print based on cells in range
    val cols = cellsInRange.keys.map(_.col).toList.distinct.sorted

    //calculate the maximum width of each column to align properly
    val colWidths = cols.map { colIndex =>
      val headerWidth = if (includeHeaders) ParseTableCells.getColName(colIndex).length else 0
      val cellWidths = filteredRows.keys.flatMap { rowIndex =>
        val cellPos = ParseTableCells(rowIndex, colIndex)
        Some(table.getEvaluatedResultAsString(cellPos).length)
      }.toSeq
      (headerWidth +: cellWidths).max
    }

    //calculate the row number column width
    val rowNumWidth = if (includeHeaders) filteredRows.keys.maxOption.map(_.toString.length).getOrElse(0) else 0

    //create the Header Row if `includeHeaders` is true
    val headerRow = {
      val headers = if (includeHeaders) {
        (Seq(padLeft("", rowNumWidth)) ++ cols.zip(colWidths).map {
          case (colIndex, width) =>
            padLeft(ParseTableCells.getColName(colIndex), width)
        }).mkString("| ", " | ", " |")
      } else {
        colWidths.map { width => padLeft("", width) }.mkString("| ", " | ", " |")
      }
      headers
    }

    //create the Separator Row if `includeHeaders` is true
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

    //build Data Rows with or without row indices
    val dataRows = filteredRows.toSeq.sortBy(_._1).map { case (rowIndex, _) =>
      val rowNumber = if (includeHeaders) padLeft(rowIndex.toString, rowNumWidth) else ""
      val rowValues = cols.zip(colWidths).map { case (colIndex, width) =>
        val cellPos = ParseTableCells(rowIndex, colIndex)
        padRight(table.getEvaluatedResultAsString(cellPos), width)
      }
      if (includeHeaders) (rowNumber +: rowValues).mkString("| ", " | ", " |")
      else rowValues.mkString("| ", " | ", " |")
    }

    //combine Header, Separator, and Data Rows
    val allRows = Seq(headerRow, separatorRow) ++ dataRows
    allRows.mkString("\n")
  }
}
