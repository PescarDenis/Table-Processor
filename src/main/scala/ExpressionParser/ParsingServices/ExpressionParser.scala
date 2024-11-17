package ExpressionParser.ParsingServices

import ExpressionAST.Expression
trait ExpressionParser {
  def parseExpression(expressionStr: String, row: Int, col: Int): Expression[?] 
}
