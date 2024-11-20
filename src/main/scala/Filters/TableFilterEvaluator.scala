package Filters

import Table.TableInterface

//knows how to evaluate a table
class TableFilterEvaluator(table: TableInterface) {

  def evaluateFilter(filter: TableFilter): List[Boolean] = {
    val rowFilterEvaluator = new RowFilterEvaluator()
    rowFilterEvaluator.evaluateFilter(table, filter)
  }
}


