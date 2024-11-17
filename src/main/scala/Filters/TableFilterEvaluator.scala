package Filters

import Table.TableInterface


class TableFilterEvaluator(table: TableInterface) {

  def evaluateFilter(filter: TableFilter): List[Boolean] = {
    val rowFilterEvaluator = new RowFilterEvaluator()
    rowFilterEvaluator.evaluateFilter(table, filter)
  }
}


