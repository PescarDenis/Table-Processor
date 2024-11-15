package Filters

import Table.TableInterface

class TableFilterEvaluator(table: TableInterface) {

  // Evaluates a specified filter against all rows in the table.
  def evaluateFilter(filter: TableFilter): List[Boolean] = {
    val rows = (1 to table.lastRow.getOrElse(0)).map(table.getRow)
    rows.map(filter.matches).toList
  }
}

