package ProcessingModules

import CLIInterface.CLIConfig
import Table.DefinedTabels.BaseTable
import Range.TableRangeEvaluator
import TableParser.ParseTableCells

class RangeSelector(config: CLIConfig, table: BaseTable) {

  def selectRange(): BaseTable = {
    val tableRangeEvaluator = new TableRangeEvaluator(table)

    val rangeResults = config.range match {
      case Some((start, end)) =>
        val fromCell = ParseTableCells.parse(start).getOrElse(
          throw new IllegalArgumentException(s"Invalid start cell reference: $start")
        )
        val toCell = ParseTableCells.parse(end).getOrElse(
          throw new IllegalArgumentException(s"Invalid end cell reference: $end")
        )
        tableRangeEvaluator.getResultsInRange(fromCell, toCell)

      case None =>
        tableRangeEvaluator.getDefaultRangeResults
    }

    // Use iterator to process the TableModel entries
    rangeResults.iterator.foreach { case (cellPos, evalResult) =>
      table.storeEvaluatedResult(cellPos, evalResult)
    }

    table // Return the modified table
  }
}
