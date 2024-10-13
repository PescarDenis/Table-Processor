package ExpressionAST

import Evaluation.EvaluationTypes.EvaluationResult
import Table.ParseTableCells

// Base trait to evaluate an expression
// It has one method -> evaluate which takes the context of the cell and the visited set to check for cyclical dependencies
trait Expression[T] {
  def evaluate(context: EvaluationContext, visited: Set[ParseTableCells]): EvaluationResult[T]
}
