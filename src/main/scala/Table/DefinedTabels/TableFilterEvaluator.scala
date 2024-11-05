package Table.DefinedTabels

import Filters.TableFilter
import Table.TableInterface
//evaluate the filters of a table
class TableFilterEvaluator(table: TableInterface) {
  //evaluates a specified filter against all rows in the table
  def evaluateFilter(filter: TableFilter): List[Boolean] = {
    val lastRow = table.lastRow.getOrElse(0) //fetch the last row
    (1 to lastRow).map { rowIndex =>
      val row = table.getRow(rowIndex) //retrive the data for the current row
      filter.matches(row) //apply the filter to the row
    }.toList //convert to result to a list of booleans
  }
}
