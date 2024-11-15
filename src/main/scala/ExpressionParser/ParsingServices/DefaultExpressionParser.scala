package ExpressionParser.ParsingServices

import ExpressionAST.Expression
import ExpressionParser.LexerLogic.Lexer
import ExpressionParser.ParserLogic.{ExpressionBuilderInterface, Parser}

class DefaultExpressionParser(expressionBuilder: ExpressionBuilderInterface[?]) extends ExpressionParser {

  override def parseExpression(exprStr: String): Expression[?] = {
    val lexer = new Lexer(exprStr)
    val tokens = lexer.tokenize()
    val parser = new Parser(tokens, expressionBuilder)
    parser.parse()
  }
}
