package Table.TableEntries

import Evaluation.EvaluationResult
import ExpressionAST.{Expression, EvaluationContext}
import ExpressionParser.ParsingServices.ExpressionParser
import ExpressionParser.ExpressionParsingError
import TableParser.ParseTableCells

// Represents a Formula class, now we pass the parser as a dependency injection inside the formula for better setting
case class Formula(row: Int, col: Int, parser: ExpressionParser) extends TableEntry {

  private var expression: Option[Expression[?]] = None

  override def get: String = expression.map(_.toString).getOrElse("")

  override def set(value: String): Unit = {
    if (value.startsWith("=")) {
      val expressionStr = value.substring(1).trim // get the expression without the =
      expression = Some(parser.parseExpression(expressionStr, row, col))
    }
    else {
      expression = Some(parser.parseExpression(value, row, col)) // We can also get raw formulas in the table
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

