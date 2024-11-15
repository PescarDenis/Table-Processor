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
      case Some((start, end)) =>
        val fromCell = ParseTableCells.parse(start).getOrElse(
          throw new IllegalArgumentException(s"Invalid start cell reference: $start")
        )
        val toCell = ParseTableCells.parse(end).getOrElse(
          throw new IllegalArgumentException(s"Invalid end cell reference: $end")
        )
        rangeEvaluator.getResultsInRange(fromCell, toCell)
      case None =>
        rangeEvaluator.getDefaultRangeResults

    }
    val evaluatedRows = range.view.mapValues(_.toValue).toMap
    val newTable = new BaseTable(table.getParser)
    newTable.initializeRows(evaluatedRows)
    newTable
}
  }

