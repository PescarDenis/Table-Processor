package ProcessingModules

import CLIInterface.CLIConfig
import Evaluation.EvaluationResult
import Evaluation.EvaluationTypes._
import Range.TableRangeEvaluator
import Table.TableModel
import TableParser.ParseTableCells
import Range.RangeError
//Range selector takes the filtered table model of evaluated results, and provide the range selection
class RangeSelector(config: CLIConfig, filteredModel: TableModel[EvaluationResult[?]]) {

  def selectRange(): TableModel[String] = {
    val tableRangeEvaluator = new TableRangeEvaluator(filteredModel) //create the TableRangeEvaluator

    val rangeResults = config.range match {
      case Some((start, end)) =>
        val fromCell = ParseTableCells.parse(start).getOrElse(
          throw new RangeError(s"Invalid start cell reference: $start") // If there is not a valid cell at the start of the range like p0-
        )
        val toCell = ParseTableCells.parse(end).getOrElse(
          throw new RangeError(s"Invalid end cell reference: $end") // If there is not a valid cell at the start of the range like Hello!
        )
        validatePositions(fromCell, toCell) // Ensures that we validate the from -> to positions
        tableRangeEvaluator.getResultsInRange(fromCell, toCell)

      case None =>
        tableRangeEvaluator.getDefaultRangeResults
    }

    // Convert EvaluationResult[_] to String
    val stringifiedResults = rangeResults.iterator.map {
      case (pos, result) => pos -> convertToString(result)
    }.toMap

    new TableModel(stringifiedResults)
  }
  
  // Helper method to get only valid cells in the table 
  private def validatePositions(fromCell: ParseTableCells, toCell: ParseTableCells): Unit = {
    val allPositions = filteredModel.toMap.keySet
    if (!allPositions.contains(fromCell)) {
      throw new RangeError(s"Start cell $fromCell does not exist in the table.")
    }
    if (!allPositions.contains(toCell)) {
      throw new RangeError(s"End cell $toCell does not exist in the table.")
    }
  }
  // After selecting the range we return a table model of strings so, this method helps us to convert the results into strings 
  private def convertToString(result: EvaluationResult[?]): String = {
    result match {
      case IntResult(value)  => value.toString
      case FloatResult(value) => value.toString
      case EmptyResult        => ""
    }
  }
}

