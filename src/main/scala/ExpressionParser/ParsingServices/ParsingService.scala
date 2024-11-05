package ExpressionParser.ParsingServices
import ExpressionAST.Expression
//interface for parsing the expression -> used for the formula
trait ParsingService[T] {
  def parseExpression(exprStr : String) : Expression[T] //method for parssing the expression used in formula
}
