package Filters

import Table.TableInterface

class RowFilterEvaluator {

  def evaluateFilter(table: TableInterface, filter: TableFilter): List[Boolean] = {
    val lastRow = table.lastRow.getOrElse(0)
    (1 to lastRow).map { rowIndex =>
      val row = table.getRow(rowIndex) // Now returns a Row
      filter.matches(row)
    }.toList
  }
}

