package Evaluation

import Evaluation.EvaluationTypes.{EvaluationResult, IntResult, EmptyResult}
import Evaluation.EvaluationError
import Table.ParseTableCells
import Table.TableEntries.{Empty, Formula, Number, TableEntry}
import ExpressionAST.{EvaluationContext, Expression}

// Base class to evaluate the table
class TableEvaluator(context: EvaluationContext) {

  // Method to evaluate a single cell
  def evaluateCell[T](cell: ParseTableCells, visited: Set[ParseTableCells] = Set.empty): EvaluationResult[T] = {
    // Check for circular dependency
    // Evaluate the cell based on its type
    context.lookup(cell) match {
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
  }

  // Method to evaluate all cells using the context's getTable method
  def evaluateAllCells(): Map[ParseTableCells, EvaluationResult[_]] = {
    val allCells = context.getTable.keys // Get all cell positions from the context's getTable
    allCells.map { cellPos =>
      cellPos -> evaluateCell[Any](cellPos) // Evaluate each cell
    }.toMap // Return the map of cell positions to evaluated results
  }
}
