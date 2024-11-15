package Evaluation

import Table.TableInterface
import ExpressionAST.EvaluationContext
import Evaluation.EvaluationTypes.EvaluationError
import TableParser.ParseTableCells

class TableEvaluator(table: TableInterface, context: EvaluationContext) {

  private val errorCells = scala.collection.mutable.ListBuffer[(ParseTableCells, String)]()

  def evaluateAllCellsAndStoreResults(): Unit = {
    table.getRawEntires.foreach { cellPos =>
      val result = table.getCell(cellPos).evaluate(context, Set.empty)
      table.storeEvaluatedResult(cellPos, result)

      result match {
        case EvaluationError(message) =>
          errorCells += (cellPos -> message)
        case _ => // No error, skip
      }
    }

    if (errorCells.nonEmpty) {
      reportErrorsAndFail()
    }
  }

  private def reportErrorsAndFail(): Unit = {
    val errorMessages = errorCells.map { case (cell, message) => s"$cell: $message" }.mkString("\n")
    throw new IllegalArgumentException(s"Evaluation failed due to errors in the following cells:\n$errorMessages")
  }
}


