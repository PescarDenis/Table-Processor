package Evaluation

import Evaluation.EvaluationTypes.{EvaluationResult, IntResult, EmptyResult}
import Table.ParseTableCells
import Table.TableEntries.{Empty, Formula, Number, TableEntry}
import ExpressionAST.{EvaluationContext, Expression}
import Table.Table

// Base class to evaluate the table
class TableEvaluator(context: EvaluationContext) {

  // Method to evaluate a single cell and store the result in the table
  def evaluateCellAndStore[T](cell: ParseTableCells, visited: Set[ParseTableCells] = Set.empty, table: Table): EvaluationResult[T] = {
    // Evaluate the cell based on its type
    val result = context.lookup(cell) match {
      case formula: Formula =>
        formula.evaluate(context, visited).asInstanceOf[EvaluationResult[T]]

      case number: Number =>
        number.numberValue match {
          case Some(value) => IntResult(value).asInstanceOf[EvaluationResult[T]]
          case None => EmptyResult.asInstanceOf[EvaluationResult[T]] // Return EmptyResult for an empty number cell
        }

      case empty: Empty =>
        EmptyResult.asInstanceOf[EvaluationResult[T]] // Return EmptyResult for a plain reference to an empty cell

      case _ =>
        throw new IllegalArgumentException(s"Invalid reference: $cell is not a valid reference")
    }

    // Store the evaluated result in the table
    table.storeEvaluatedResult(cell, result)

    result
  }

  // Method to evaluate all cells and store the results in the table
  def evaluateAllCellsAndStoreResults(table: Table): Unit = {
    context.getTable.keys.foreach { cellPos =>
      evaluateCellAndStore[Any](cellPos, Set.empty, table) // Evaluate and store each cell's result
    }
  }
}
