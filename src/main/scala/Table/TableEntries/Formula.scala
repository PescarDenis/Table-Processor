package Table.TableEntries

import Evaluation.EvaluationResult
import ExpressionAST.{Expression, EvaluationContext}
import ExpressionParser.ParsingServices.ExpressionParser
import TableParser.ParseTableCells


case class Formula(row: Int, col: Int, parser: ExpressionParser) extends TableEntry {

  private var expression: Option[Expression[?]] = None

  override def get: String = expression.map(_.toString).getOrElse("")

  override def set(value: String): Unit = {
    if (value.startsWith("=")) {
      val expressionStr = value.substring(1).trim
      try {
        expression = Some(parser.parseExpression(expressionStr, row, col))
      } catch {
        case e: IllegalArgumentException =>
          throw new IllegalArgumentException(
            s"${e.getMessage}"
          )
      }
    } else {
      expression = Some(parser.parseExpression(value, row, col))
    }
  }

  override def isEmpty: Boolean = expression.isEmpty

  override def evaluate(context: EvaluationContext, visited: Set[ParseTableCells]): EvaluationResult[?] = {
    expression match {
      case Some(expr) => expr.evaluate(context, visited)
      case None       => throw new IllegalStateException(s"No expression set for cell: ($row, $col)")
    }
  }
}

