package Evaluation

import Table.TableInterface
import ExpressionAST.EvaluationContext
import Evaluation.EvaluationTypes.EvaluationError
import TableParser.ParseTableCells

//Table evaluator class that knows how to evaluate a table
class TableEvaluator(table: TableInterface, context: EvaluationContext) {

  //create a mutable list to collect the error cells
  private val errorCells = scala.collection.mutable.ListBuffer[(ParseTableCells, String)]()

  //method to evaluate the cells and store the results
  def evaluateAllCellsAndStoreResults(): Unit = {
    table.getRawEntires.foreach { cellPos => //iterate through the raw entries
      val result = table.getCell(cellPos).evaluate(context, Set.empty) //evaluate each cell
      table.storeEvaluatedResult(cellPos, result) //store the result

      result match {
        case EvaluationError(message) =>
          errorCells += (cellPos -> message) //collect the error messages
        case _ => // No error, skip
      }
    }

    if (errorCells.nonEmpty) {
      reportErrorsAndFail() //if there are errors, fail the evaluation
    }
  }

  private def reportErrorsAndFail(): Unit = {
    val errorMessages = errorCells.map { case (cell, message) => s"$cell: $message" }.mkString("\n") //create the error messages
    throw TableEvaluatorError(s"Evaluation failed due to errors in the following cells:\n$errorMessages")
  }
}


