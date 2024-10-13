package ExpressionParser

sealed trait Tokens //base type for all tokens
case class NumberToken(value : String) extends Tokens //represents a number found in the formula
case class OperatorToken(op : String) extends Tokens //represents the operators used in the formula...
case class RefToken(ref : String) extends Tokens //represents a cell in the table D20 or so...
case object LeftParenthesis extends Tokens // (
case object RightParenthesis extends Tokens // )




