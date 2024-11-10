package ProcessingModules

import ExpressionAST.EvaluationContext
import Table.TableInterfaces._
import Table.TableEntries.TableEntry
import Evaluation.TableEvaluator
import Evaluation.EvaluationTypes.EvaluationResult

class Evaluator(
                 rawTable: RawTableInterface[TableEntry],
                 evaluatedTable: EvaluatedTableInterface[EvaluationResult[_]]
               ) {

  def evaluateAll(): Unit = {
    val tableEvaluator = new TableEvaluator()
    val evaluationContext = new EvaluationContext(rawTable, tableEvaluator)

    // Use TableEvaluator from the context
    evaluationContext.getTableEvaluator.evaluateAllCellsAndStoreResults(
      rawTable,
      evaluatedTable
    )
  }
}
