package Table.DefinedTabels

import Filters.TableFilter
import Table.TableInterface

class TableFilterEvaluator(table: TableInterface) {

  def evaluateFilter(filter: TableFilter): List[Boolean] = {
    val lastRow = table.lastRow.getOrElse(0)
    (1 to lastRow).map { rowIndex =>
      val row = table.getRow(rowIndex)
      filter.matches(row)
    }.toList
  }
}
