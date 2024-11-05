package ProcessingModules

import CLIInterface.CLIConfig
import Table.DefinedTabels.{BaseTable, TableRange}
import Table.ParseTableCells
//selects a specific range of cells from the table
class RangeSelector(config: CLIConfig, table: BaseTable) {
  def selectRange(): BaseTable = {
    val tableRange = new TableRange(table)
    config.range match {
      case Some((start, end)) =>
        val fromCell = ParseTableCells.parse(start).getOrElse(
          throw new IllegalArgumentException(s"Invalid start cell reference: $start")
        )
        val toCell = ParseTableCells.parse(end).getOrElse(
          throw new IllegalArgumentException(s"Invalid end cell reference: $end")
        )
        val rangeMap = tableRange.getRange(fromCell, toCell)
        rangeMap.foreach { case (cellPos, result) =>
          table.storeEvaluatedResult(cellPos, result)
        }
        table

      case None =>
        val defaultRangeMap = tableRange.getDefaultRange
        defaultRangeMap.foreach { case (cellPos, result) =>
          table.storeEvaluatedResult(cellPos, result)
        }
        table
    }
  }
}

