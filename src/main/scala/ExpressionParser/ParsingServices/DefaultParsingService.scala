package ExpressionParser.ParsingServices
import ExpressionParser.ParserLogic._
import ExpressionParser.LexerLogic._
import ExpressionAST.Expression
class DefaultParsingService[T](expressionBuilder : ExpressionBuilder[T]) extends ParsingService[T] {
  override def parseExpression(exprStr: String): Expression[T] = {
    val lexer = new Lexer(exprStr)
    val tokens = lexer.tokenize()
    val parser = new Parser(tokens, expressionBuilder)
    parser.parse()
  }
}
