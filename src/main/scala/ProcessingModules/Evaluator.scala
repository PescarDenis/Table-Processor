package ProcessingModules

import Evaluation.*
import ExpressionAST.EvaluationContext
import Table.DefinedTabels.BaseTable
import Table.TableInterfaces._
import Evaluation.EvaluationTypes.EvaluationResult
import Table.TableEntries.TableEntry
class Evaluator(rawTable: RawTableInterface[TableEntry], evaluatedTable: EvaluatedTableInterface[EvaluationResult[_]]) {

  def evaluateAll(): Unit = {
    val evaluationContext = new EvaluationContext(rawTable) // Only raw data is needed for context
    val tableEvaluator = new TableEvaluator(evaluationContext)
    tableEvaluator.evaluateAllCellsAndStoreResults(rawTable, evaluatedTable) // Separate raw and evaluated tables
  }
}

