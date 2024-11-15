package Evaluation
import Table.TableInterface
import ExpressionAST.EvaluationContext
class TableEvaluator(table: TableInterface, context: EvaluationContext) {

  def evaluateAllCellsAndStoreResults(): Unit = {
    table.getRawEntires.foreach { cellPos =>
      val result = table.getCell(cellPos).evaluate(context, Set.empty)
      table.storeEvaluatedResult(cellPos, result)
    }
  }
}

