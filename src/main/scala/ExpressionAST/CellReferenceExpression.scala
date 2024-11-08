package ExpressionAST
import Evaluation.EvaluationTypes.{EvaluationResult, IntResult,EmptyResult}
import Table.ParseTableCells
import Table.TableEntries.{Formula, Number, Empty}
// Case class for the reference cells
case class CellReferenceExpression[T](cell: ParseTableCells) extends Expression[T] {

  // Check for circular dependencies and evaluate the referenced cell
  override def evaluate(context: EvaluationContext, visited: Set[ParseTableCells]): EvaluationResult[T] = {
    if (visited.contains(cell)) {
      throw new IllegalArgumentException(s"Evaluation error : Circular dependency detected at cell: $cell")
    }
    val updatedVisited = visited + cell // Update the set with the visited cells
    val entry = context.lookup(cell) // Match the entry based on what we find inside the cell

    entry match {
      case numberEntry: Number =>
        IntResult(numberEntry.numberValue.getOrElse(throw new IllegalArgumentException(s"Cell $cell is empty"))).asInstanceOf[EvaluationResult[T]]
      // If it is a number, get the number value. It will always be an int
      case formulaEntry: Formula[T] =>
        formulaEntry.evaluate(context, updatedVisited).asInstanceOf[EvaluationResult[T]]
      // If it is a formula, evaluate it and return the result

      case _: Empty =>
        EmptyResult.asInstanceOf[EvaluationResult[T]] // Return EmptyResult instead of throwing an error for plain references

      case _ =>
        throw new IllegalArgumentException(s"Invalid reference: $cell is not a valid reference")
    }
  }
}