package ProcessingModules

import CLIInterface.CLIConfig
import Evaluation.EvaluationResult
import Evaluation.EvaluationTypes._
import Range.TableRangeEvaluator
import Table.TableModel
import TableParser.ParseTableCells
import Range.RangeError
class RangeSelector(config: CLIConfig, filteredModel: TableModel[EvaluationResult[?]]) {

  def selectRange(): TableModel[String] = {
    val tableRangeEvaluator = new TableRangeEvaluator(filteredModel)

    val rangeResults = config.range match {
      case Some((start, end)) =>
        val fromCell = ParseTableCells.parse(start).getOrElse(
          throw new RangeError(s"Invalid start cell reference: $start")
        )
        val toCell = ParseTableCells.parse(end).getOrElse(
          throw new RangeError(s"Invalid end cell reference: $end")
        )
        validatePositions(fromCell, toCell)
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

  private def validatePositions(fromCell: ParseTableCells, toCell: ParseTableCells): Unit = {
    val allPositions = filteredModel.toMap.keySet
    if (!allPositions.contains(fromCell)) {
      throw new RangeError(s"Start cell $fromCell does not exist in the table.")
    }
    if (!allPositions.contains(toCell)) {
      throw new RangeError(s"End cell $toCell does not exist in the table.")
    }
  }

  private def convertToString(result: EvaluationResult[?]): String = {
    result match {
      case IntResult(value)  => value.toString
      case FloatResult(value) => value.toString
      case EmptyResult        => ""
    }
  }
}

