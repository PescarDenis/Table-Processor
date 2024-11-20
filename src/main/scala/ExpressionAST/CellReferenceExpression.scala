package ExpressionAST

import Evaluation.EvaluationTypes.EvaluationError
import Evaluation.EvaluationResult
import Table.TableEntries.TableEntry
import TableParser.ParseTableCells

// Detects the reference of the cells in a formula and gets the cyclical  dependencies detected at the recursive call
case class CellReferenceExpression[T](cell: ParseTableCells) extends Expression[T] {

  override def evaluate(context: EvaluationContext, visited: Set[ParseTableCells]): EvaluationResult[T] = {
    if (visited.contains(cell)) {
      EvaluationError(s"Circular dependency detected at cell: $cell") //If we already have the cell in the set it means that we detected a cyclical dependency
    } else {
      val entry: TableEntry = context.lookup(cell) // Get the next table entry
      entry.evaluate(context, visited + cell).asInstanceOf[EvaluationResult[T]] // Go and check for the other cyclical  dependencies
    }
  }
}


