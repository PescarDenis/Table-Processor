package ExpressionAST

import Evaluation.EvaluationResult
import TableParser.ParseTableCells

trait Expression[T] {
  def evaluate(context: EvaluationContext, visited: Set[ParseTableCells]): EvaluationResult[T]
}
