package ProcessingModules

import CLIInterface.CLIConfig
import Filters.TableFilterEvaluator
import Table.DefinedTabels.BaseTable

//applies a set of filters from the CLIConfig to the table
//each filter is evaluated and the results are applied to the table
class Filtering(config: CLIConfig, table: BaseTable) {
  def applyFilters(): Unit = {
    val tableFilterEvaluator = new TableFilterEvaluator(table)
    config.filters.foreach { filter =>
      tableFilterEvaluator.evaluateFilter(filter)
    }
  }
}

