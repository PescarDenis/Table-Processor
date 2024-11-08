package PrettyPrint

import Table.{ParseTableCells, TableInterfaces}
import File_Reader.CSVSeparator
import Table.DefinedTabels._
import Filters.TableFilter
//CSV printer class -> knows how to print the table in the CSV format
class CSVPrettyPrinter(separator: CSVSeparator) extends PrettyPrinter {
  override def print(
                      table: TableInterface,
                      range: Option[(ParseTableCells, ParseTableCells)],
                      filter: Option[TableFilter],
                      includeHeaders: Boolean
                    ): String = {
    val sep = separator.CellSeparator + " " //add a white space for better indentation and to proper handle a CSV file
    val tableRange = new TableRange(table) //helper variable to manage table range and filtering
    val tableFilterEvaluator = new TableFilterEvaluator(table)

    //determine range - full table if None
    val effectiveRange = range.getOrElse {
      val maxRow = table.lastRow.getOrElse(1)
      val maxCol = table.lastColumn.getOrElse(1)
      (ParseTableCells(1, 1), ParseTableCells(maxRow, maxCol))
    }

    //get cells within the specified or default range
    val cellsInRange = tableRange.getRange(effectiveRange._1, effectiveRange._2)

    //apply filter if specified, otherwise include all rows
    val filteredRows = filter match {
      case Some(f) =>
        val filterResults = tableFilterEvaluator.evaluateFilter(f)
        val matchingRowIndices = filterResults.zipWithIndex.collect {
          case (true, idx) => idx + 1 // Row indices are 1-based
        }.toSet
        cellsInRange.groupBy(_._1.row).filter { case (rowIndex, _) => matchingRowIndices.contains(rowIndex) }
      case None => cellsInRange.groupBy(_._1.row)
    }

    if (filteredRows.isEmpty) return "" //early exit if no data rows are available

    //determine columns to print based on cells in range
    val cols = cellsInRange.keys.map(_.col).toList.distinct.sorted

    //construct the header row if headers are included
    val headerRow = if (includeHeaders) {
      val colHeaders = cols.map(colIndex => ParseTableCells.getColName(colIndex))
      ("" +: colHeaders).mkString(sep)
    } else ""

    //build data rows, including row numbers if headers are included
    val dataRows = filteredRows.toSeq.sortBy(_._1).map { case (rowIndex, _) =>
      val rowCells = cols.map { colIndex =>
        val cellPos = ParseTableCells(rowIndex, colIndex)
        table.getEvaluatedResultAsString(cellPos)
      }
      if (includeHeaders) (rowIndex.toString +: rowCells).mkString(sep)
      else rowCells.mkString(sep)
    }

    //combine header and data rows into the final output
    val allRows = if (includeHeaders) Seq(headerRow) ++ dataRows else dataRows
    allRows.mkString("\n")
  }
}
