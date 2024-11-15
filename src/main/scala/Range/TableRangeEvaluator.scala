package Range

import Evaluation.EvaluationResult
import Table.{TableInterface, TableModel}
import TableParser.ParseTableCells

// Evaluates ranges by interacting with the table's existing interface
class TableRangeEvaluator(table: TableInterface) {

  private val rangeSelector = new TableRange()

  // Generalized method to get results for a set of positions as a TableModel
  private def getResultsForPositions(positions: List[ParseTableCells]): TableModel[EvaluationResult[?]] = {
    val results = positions.flatMap(pos => table.getEvaluatedResult(pos).map(pos -> _)).toMap
    new TableModel(results)
  }

  // Get evaluated results for a specified range as a TableModel
  def getResultsInRange(from: ParseTableCells, to: ParseTableCells): TableModel[EvaluationResult[?]] = {
    val positions = rangeSelector.getRange(from, to)
    getResultsForPositions(positions)
  }

  // Get evaluated results for the default range as a TableModel
  def getDefaultRangeResults: TableModel[EvaluationResult[?]] = {
    val positions = rangeSelector.getDefaultRange(table.nonEmptyPositions)
    getResultsForPositions(positions)
  }
}
