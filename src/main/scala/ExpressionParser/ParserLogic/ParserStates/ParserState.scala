package ExpressionParser.ParserLogic.ParserStates

import ExpressionAST.Expression

// Define the case class for the current state of the parser, it contains the curretn expression
// the current operator and a boolean variable to see if the last token parsed was an operator
case class ParserState[T](currentExpression: Option[Expression[T]] = None, lastWasOperator: Boolean = true, lastOperator: String = "")
