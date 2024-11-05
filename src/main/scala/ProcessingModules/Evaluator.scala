package ProcessingModules

import Evaluation.*
import ExpressionAST.EvaluationContext
import Table.DefinedTabels.BaseTable

//responsible for evaluating all the cells in the given table
class Evaluator(table: BaseTable) {
  def evaluateAll(): Unit = {
    val evaluationContext = new EvaluationContext(table.getTable)
    val tableEvaluator = new TableEvaluator(evaluationContext)
    tableEvaluator.evaluateAllCellsAndStoreResults(table)
  }
}

