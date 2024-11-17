package Filters

import Table.TableInterface

//knows how to evaluate a table
class TableFilterEvaluator(table: TableInterface) {

  def evaluateFilter(filter: TableFilter): List[Boolean] = {
    try { //tries to evaluate a given table
      val rowFilterEvaluator = new RowFilterEvaluator()
      rowFilterEvaluator.evaluateFilter(table, filter)
    }
    catch{ //otherwise logs the error and return an empty list of booleans
      case e: FilterError =>
        List.empty[Boolean] //return an empty list
    }
  }
}


