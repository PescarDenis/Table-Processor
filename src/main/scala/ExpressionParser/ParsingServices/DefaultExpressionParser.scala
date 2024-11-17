package ExpressionParser.ParsingServices

import ExpressionAST.Expression
import ExpressionParser.LexerLogic.Lexer
import ExpressionParser.ParserLogic.{ExpressionBuilderInterface, Parser}

class DefaultExpressionParser(expressionBuilder: ExpressionBuilderInterface[?]) extends ExpressionParser {

  override def parseExpression(expressionStr: String, row: Int, col: Int): Expression[?] = {
    val lexer = new Lexer(expressionStr, row, col)
    val tokens = lexer.tokenize()
    val parser = new Parser(tokens, expressionBuilder, row, col)
    parser.parse()
  }
}
