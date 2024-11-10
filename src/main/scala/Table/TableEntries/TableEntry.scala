package Table.TableEntries

import Evaluation.EvaluationResult
import ExpressionAST.EvaluationContext
import TableParser.ParseTableCells

trait TableEntry {
  def get: String  // Return a string representation of the value
  def set(value: String): Unit // Set the value, parsing if necessary
  def isEmpty: Boolean // Check if the cell is empty
  def evaluate(context: EvaluationContext, visited: Set[ParseTableCells]): EvaluationResult[?] // Evaluate cell content
}

