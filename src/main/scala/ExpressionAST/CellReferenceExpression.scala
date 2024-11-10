package ExpressionAST

import Evaluation.EvaluationTypes.{EmptyResult, IntResult}
import Evaluation.EvaluationResult
import Table.TableEntries.{Empty, Formula, Number, TableEntry}
import TableParser.ParseTableCells


case class CellReferenceExpression[T](cell: ParseTableCells) extends Expression[T] {

  override def evaluate(context: EvaluationContext, visited: Set[ParseTableCells]): EvaluationResult[T] = {
    if (visited.contains(cell)) {
      throw new IllegalArgumentException(s"Circular dependency detected at cell: $cell")
    }

    val entry: TableEntry = context.lookup(cell)
    entry.evaluate(context, visited + cell).asInstanceOf[EvaluationResult[T]]
  }
}


