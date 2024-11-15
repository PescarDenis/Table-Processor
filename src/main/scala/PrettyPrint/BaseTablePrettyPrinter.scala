package PrettyPrint

import Evaluation.EvaluationResult
import Table.{TableInterface, TableModel}
import Filters.{RowFilterEvaluator, TableFilter}
import Range.TableRangeEvaluator
import TableParser.ParseTableCells
import File_Reader.CSVSeparator

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

  // Fetch and filter rows as strings using TableModel
  protected def getFilteredRows(
                                 table: TableInterface,
                                 range: (ParseTableCells, ParseTableCells),
                                 filter: Option[TableFilter]
                               ): TableModel[String] = {
    val tableRangeEvaluator = new TableRangeEvaluator(table)
    val tableFilterEvaluator = new RowFilterEvaluator

    val rangeModel = tableRangeEvaluator.getResultsInRange(range._1, range._2)
    val filteredRows = filter match {
      case Some(f) =>
        val filterResults = tableFilterEvaluator.evaluateFilter(table, f)
        val matchingRowIndices = filterResults.zipWithIndex.collect {
          case (true, idx) => idx + 1
        }.toSet

        rangeModel.toMap.view.filterKeys(pos => matchingRowIndices.contains(pos.row)).toMap
      case None => rangeModel.toMap
    }

    // Convert results to strings and construct a new TableModel
    val stringifiedResults = filteredRows.map {
      case (pos, _) => pos -> table.getEvaluatedResultAsString(pos)
    }

    new TableModel(stringifiedResults)
  }

  // Abstract methods to be implemented by subclasses
  protected def buildHeaders(
                              cols: Seq[Int],
                              includeHeaders: Boolean
                            ): Option[String]

  protected def buildRows(
                           rows: TableModel[String],
                           includeRowNumbers: Boolean
                         ): Seq[String]
}
