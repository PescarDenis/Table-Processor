package ProcessingModules

import CLIInterface.CLIConfig
import Evaluation.EvaluationResult
import Filters.TableFilterEvaluator
import Table.DefinedTabels.BaseTable
import Table.TableModel

//applies a set of filters from the CLIConfig to the table
//each filter is evaluated and the results are applied to the table
class Filtering(config: CLIConfig, table: BaseTable) {

  def applyFilters(): TableModel[EvaluationResult[?]] = {
    val tableFilterEvaluator = new TableFilterEvaluator(table)

    // Evaluate each filter and collect the rows that pass all filters
    val filteredRowIndices = config.filters.foldLeft((1 to table.lastRow.getOrElse(0)).toSet) { (matchingRows, filter) =>
      val rowMatches = tableFilterEvaluator.evaluateFilter(filter).zipWithIndex.collect {
        case (true, idx) => idx + 1 // Collect matching row indices
      }.toSet
      matchingRows intersect rowMatches
    }

    // Keep only matching rows
    val filteredCells = table.nonEmptyPositions.filter(cell => filteredRowIndices.contains(cell.row))
    val filteredData = filteredCells.map(cell => cell -> table.getEvaluatedResult(cell).get).toMap

    new TableModel(filteredData)
  }
}

