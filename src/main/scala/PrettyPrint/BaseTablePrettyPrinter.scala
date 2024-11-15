package PrettyPrint

import Evaluation.EvaluationResult
import Table.TableInterface
import Filters.{RowFilterEvaluator, TableFilter}
import Range.TableRangeEvaluator
import TableParser.ParseTableCells
import File_Reader.CSVSeparator
import Filters.RowFilterEvaluator
abstract class BaseTablePrettyPrinter extends PrettyPrinter {

  // Determine the effective range for the table
  protected def getEffectiveRange(
                                   table: TableInterface,
                                   range: Option[(ParseTableCells, ParseTableCells)]
                                 ): (ParseTableCells, ParseTableCells) = {
    range.getOrElse {
      val maxRow = table.lastRow.getOrElse(1)
      val maxCol = table.lastColumn.getOrElse(1)
      (ParseTableCells(1, 1), ParseTableCells(maxRow, maxCol))
    }
  }

  // Fetch and filter rows as strings
  protected def getFilteredRows(
                                 table: TableInterface,
                                 range: (ParseTableCells, ParseTableCells),
                                 filter: Option[TableFilter]
                               ): Map[Int, Map[ParseTableCells, String]] = {
    val tableRange = new TableRangeEvaluator(table)
    val tableFilterEvaluator = new RowFilterEvaluator
    val rangeResults = tableRange.getResultsInRange(range._1, range._2)

    val filteredRows = filter match {
      case Some(f) =>
        val filterResults = tableFilterEvaluator.evaluateFilter(table,f)
        val matchingRowIndices = filterResults.zipWithIndex.collect {
          case (true, idx) => idx + 1
        }.toSet
        rangeResults.groupBy(_._1.row).filter { case (rowIndex, _) => matchingRowIndices.contains(rowIndex) }
      case None => rangeResults.groupBy(_._1.row)
    }

    filteredRows.map { case (rowIdx, cells) =>
      rowIdx -> cells.toMap.map { case (cell, _) => cell -> table.getEvaluatedResultAsString(cell) }
    }
  }

  protected def buildHeaders(
                              cols: Seq[Int],
                              includeHeaders: Boolean
                            ): Option[String]

  protected def buildRows(
                           rows: Map[Int, Map[ParseTableCells, String]],
                           includeRowNumbers: Boolean
                         ): Seq[String]
}
