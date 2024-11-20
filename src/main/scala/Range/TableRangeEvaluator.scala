package Range

import Evaluation.EvaluationResult
import Evaluation.EvaluationTypes.EmptyResult
import Table.TableModel
import TableParser.ParseTableCells

// Evaluates ranges by interacting with the table's existing interface
class TableRangeEvaluator(model: TableModel[EvaluationResult[?]]) {

  private val rangeSelector = new TableRange()

  // Generalized method to get results for a set of positions as a TableModel
  private def getResultsForPositions(positions: List[ParseTableCells]): TableModel[EvaluationResult[?]] = {
    val results = positions.flatMap(pos => model.getCell(pos).map(pos -> _)).toMap
    new TableModel(results)
  }

  // Get evaluated results for a specified range as a TableModel
  def getResultsInRange(from: ParseTableCells, to: ParseTableCells): TableModel[EvaluationResult[?]] = {
    val positions = rangeSelector.getRange(from, to)
    getResultsForPositions(positions)
  }

  // Get default range results based on non-empty cells
  def getDefaultRangeResults: TableModel[EvaluationResult[?]] = {
    // Filter out positions where the cell value is EmptyResult
    val nonEmptyFilteredPositions = model.toMap.collect {
      case (pos, value) if value != EmptyResult => pos
    }
    // Get the default range from the filtered positions
    val positions = rangeSelector.getDefaultRange(nonEmptyFilteredPositions)
    getResultsForPositions(positions)
  }

}
