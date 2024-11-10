package ExpressionAST
import Evaluation.EvaluationTypes.{EvaluationResult, IntResult,EmptyResult}
import Table.ParseTableCells
import Table.TableEntries.{Formula, Number, Empty}

// Case class for the reference cells
case class CellReferenceExpression[T](cell: ParseTableCells) extends Expression[T] {

  override def evaluate(context: EvaluationContext, visited: Set[ParseTableCells]): EvaluationResult[T] = {
    // Delegate to the TableEvaluator (via EvaluationContext)
    context.getTableEvaluator.evaluateCell(cell, visited).asInstanceOf[EvaluationResult[T]]
  }
}
