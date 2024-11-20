package ExpressionParser.ParsingServices

import ExpressionAST.Expression
// Trait for parsing an expression
trait ExpressionParser {
  def parseExpression(expressionStr: String, row: Int, col: Int): Expression[?] 
}
