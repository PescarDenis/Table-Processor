package Table.TableEntries

import Evaluation.EvaluationTypes.EvaluationResult
import ExpressionAST.{Expression, EvaluationContext}
import ExpressionParser.ParsingServices._
import Table.ParseTableCells

// Formula class -> handles formulas or raw implementations
class Formula[T](row: Int, col: Int, parsingService: ParsingService[T]) extends TableEntry(row, col) {

  private var expression: Option[Expression[T]] = None // Store the parsed Expression

  override def get: String = expression.map(_.toString).getOrElse("")

  override def set(value: String): Unit = {
    val exprStr = if (value.startsWith("=")) value.substring(1).trim else value.trim
    expression = Some(parsingService.parseExpression(exprStr)) // Parse using the ParsingService
  }

  override def isEmpty: Boolean = expression.isEmpty

  // Evaluate the expression
  def evaluate(context: EvaluationContext, visited: Set[ParseTableCells]): EvaluationResult[T] = {
    expression match {
      case Some(expr) => expr.evaluate(context, visited)
      case None       => throw new IllegalStateException("No expression set for this formula")
    }
  }
}
