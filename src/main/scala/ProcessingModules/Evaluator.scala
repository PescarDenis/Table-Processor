package ProcessingModules


import ExpressionAST.EvaluationContext
import Table.DefinedTabels.BaseTable
import Evaluation.TableEvaluator

// Evaluates all cells in the provided table.
class Evaluator(table: BaseTable) {
  def evaluateAll(): Unit = {
    val context = new EvaluationContext(table)
    val evaluator = new TableEvaluator(table, context)
    evaluator.evaluateAllCellsAndStoreResults()
  }
}


 
