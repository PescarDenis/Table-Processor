package ExpressionParser.ParsingServices

import ExpressionAST.Expression
import ExpressionParser.LexerLogic.Lexer
import ExpressionParser.ParserLogic.ExpressionBuilder.ExpressionBuilderInterface
import ExpressionParser.ParserLogic.ParserStates.Parser

// Knows how to parse an expression
class DefaultExpressionParser(expressionBuilder: ExpressionBuilderInterface[?]) extends ExpressionParser {

  override def parseExpression(expressionStr: String, row: Int, col: Int): Expression[?] = {
    val lexer = new Lexer(expressionStr, row, col) //Create the lexer
    val tokens = lexer.tokenize() // Create the token list
    val parser =  Parser(tokens, expressionBuilder, row, col) // Create the Parser
    parser.parse() // Parser the expression
  }
}
