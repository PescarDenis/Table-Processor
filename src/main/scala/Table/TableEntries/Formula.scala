package Table.TableEntries

import Evaluation.EvaluationTypes._
import ExpressionAST.{Expression, EvaluationContext}
import ExpressionParser.ParsingServices._
import Table.ParseTableCells

class Formula(row: Int, col: Int, parsingService: ParsingService[_]) extends TableEntry(row, col) {

  // Properly initialized to None to indicate no expression has been set yet
  private var expression: Option[Expression[_]] = None

  // Get a string representation of the current expression or empty string if none is set
  override def get: String = expression.map(_.toString).getOrElse("")

  // Parse and set the raw formula value into an expression
  override def set(value: String): Unit = {
    val exprStr = if (value.startsWith("=")) value.substring(1).trim else value.trim
    expression = Some(parsingService.parseExpression(exprStr)) // Store parsed expression
  }

  // Checks if no expression has been set
  override def isEmpty: Boolean = expression.isEmpty

  // Returns the current expression, if any
  def getExpression: Option[Expression[_]] = expression

  def cellPosition: ParseTableCells = ParseTableCells(row, col)
}