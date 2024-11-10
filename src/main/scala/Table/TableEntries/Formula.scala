package Table.TableEntries

import Evaluation.EvaluationResult
import ExpressionAST.{Expression, EvaluationContext}
import ExpressionParser.ParsingServices.ExpressionParser
import TableParser.ParseTableCells


case class Formula(row: Int, col: Int, parser: ExpressionParser) extends TableEntry {

  private var expression: Option[Expression[?]] = None // Store the parsed Expression

  override def get: String = expression.map(_.toString).getOrElse("")

  override def set(value: String): Unit = {
    if (value.startsWith("=")) {
      val expressionStr = value.substring(1).trim
      // Parse the string into an expression using a parser
      expression = Some(parser.parseExpression(expressionStr))
    } else {
      expression = Some(parser.parseExpression(value)) // Parse the raw expression, for instance 10*25
    }
  }
  
  override def isEmpty: Boolean = expression.isEmpty

  // Evaluate the stored expression
  override def evaluate(context: EvaluationContext, visited: Set[ParseTableCells]): EvaluationResult[?] = {
    expression match {
      case Some(expr) => expr.evaluate(context, visited) // Call evaluate on the contained expression
      case None => throw new IllegalStateException(s"No expression set for cell: (${row}, ${col})")
    }
  }
}
