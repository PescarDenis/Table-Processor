package Range


import Evaluation.EvaluationResult
import Table.TableInterface
import TableParser.ParseTableCells

// Evaluates ranges by interacting with the table's existing interface
class TableRangeEvaluator(table: TableInterface) {

  private val rangeSelector = new TableRange()

  // Get evaluated results for a specified range
  def getResultsInRange(from: ParseTableCells, to: ParseTableCells): Map[ParseTableCells, EvaluationResult[?]] = {
    val positions = rangeSelector.getRange(from, to)
    positions.flatMap(pos => table.getEvaluatedResult(pos).map(pos -> _)).toMap
  }

  // Get evaluated results for the default range (non-empty positions)
  def getDefaultRangeResults: Map[ParseTableCells, EvaluationResult[?]] = {
    val positions = rangeSelector.getDefaultRange(table.nonEmptyPositions)
    positions.flatMap(pos => table.getEvaluatedResult(pos).map(pos -> _)).toMap
  }
}

