package Filters

import Filters.TableFilter
import Table.TableInterface
import TableParser.ParseTableCells

// Evaluate filters independently
class RowFilterEvaluator {
  
    // Evaluate a filter for each row, returning true/false for each row
    def evaluateFilter(table: TableInterface, filter: TableFilter): List[Boolean] = {
      val lastRow = table.lastRow.getOrElse(0)
      (1 to lastRow).map { rowIndex =>
        val row = table.getRow(rowIndex)
        filter.matches(row) // Apply the filter to each row
      }.toList
    }
  }