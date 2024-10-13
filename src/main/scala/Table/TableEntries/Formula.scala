package Table.TableEntries
import Evaluation.EvaluationTypes.EvaluationResult
import ExpressionAST.{Expression, EvaluationContext}
import ExpressionParser.{Lexer, Parser}
import Table.ParseTableCells

// Formula class for handling either formulas (e.g., =10*2) or raw implementations like 20*3
class Formula(row: Int, col: Int) extends TableEntry(row, col) {

  private var expression: Option[Expression[_]] = None // Store the parsed Expression

  override def get: String = expression.map(_.toString).getOrElse("")

  override def set(value: String): Unit = {
    if (value.startsWith("=")) {
      val expressionStr = value.substring(1).trim
      // Parse the string into an expression using a parser
      expression = Some(parseExpression(expressionStr))
    } else {
      expression = Some(parseExpression(value)) // Parse the raw expression, for instance 10*25
    }
  }

  override def isEmpty: Boolean = expression.isEmpty

  // Evaluate the expression
  def evaluate(context: EvaluationContext, visited: Set[ParseTableCells]): EvaluationResult[_] = {
    expression match {
      case Some(expr) => expr.evaluate(context, visited) // Evaluate the expression
      case None => throw new IllegalStateException("No expression set for this formula")
    }
  }

  // Helper method to parse the expression
  private def parseExpression(exprStr: String): Expression[_] = {
    val lexer = new Lexer(exprStr)
    val tokens = lexer.tokenize()
    val parser = new Parser(tokens)
    parser.parse() // Return the parsed Expression
  }
}