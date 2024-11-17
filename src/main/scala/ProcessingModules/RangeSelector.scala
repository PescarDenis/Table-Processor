package ProcessingModules

import CLIInterface.CLIConfig
import Evaluation.EvaluationResult
import Evaluation.EvaluationTypes._
import Range.TableRangeEvaluator
import Table.TableModel
import TableParser.ParseTableCells
import Table.DefinedTabels.BaseTable

class RangeSelector(config: CLIConfig, filteredModel: TableModel[EvaluationResult[_]]) {

  def selectRange(): TableModel[String] = {
    val tableRangeEvaluator = new TableRangeEvaluator(filteredModel)

    val rangeResults = config.range match {
      case Some((start, end)) =>
        val fromCell = ParseTableCells.parse(start).getOrElse(
          throw new IllegalArgumentException(s"Invalid start cell reference: $start")
        )
        val toCell = ParseTableCells.parse(end).getOrElse(
          throw new IllegalArgumentException(s"Invalid end cell reference: $end")
        )
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

  private def convertToString(result: EvaluationResult[_]): String = {
    result match {
      case IntResult(value)  => value.toString
      case FloatResult(value) => value.toString
      case EmptyResult        => ""
      case _                 => "ERROR" // This should never happen with valid evaluations
    }
  }
}

