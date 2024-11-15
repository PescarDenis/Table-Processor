package ProcessingModules

import CLIInterface.CLIConfig
import Table.DefinedTabels.BaseTable
import Range.TableRangeEvaluator
import TableParser.ParseTableCells


// Selects a specific range of cells from the table.
class RangeSelector(config: CLIConfig, table: BaseTable) {

  def selectRange(): BaseTable = {
    val rangeEvaluator = new TableRangeEvaluator(table)
    val range = config.range match {
      case Some((fromCell, toCell)) =>
        rangeEvaluator.getResultsInRange(fromCell, toCell)
      case None =>
        rangeEvaluator.getDefaultRangeResults
    }

    // Using `.view.mapValues(f).toMap` to avoid the deprecated symbol
    val evaluatedRows = range.view.mapValues(_.toEntry).toMap

    val newTable = new BaseTable(table.parser)
    newTable.initializeRows(evaluatedRows)
    newTable
  }
}

