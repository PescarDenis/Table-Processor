package ExpressionParser.ParsingServices

import ExpressionAST.Expression
trait ExpressionParser {
    def parseExpression(exprStr : String) : Expression[?]
}
