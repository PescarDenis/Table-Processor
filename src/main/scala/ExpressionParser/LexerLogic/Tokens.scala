package ExpressionParser.LexerLogic


sealed trait Tokens //the sealed trait is used as an Enum in scala
case class NumberToken(value : String) extends Tokens //represents a number found in the formula
case class OperatorToken(op : String) extends Tokens //represents the operators used in the formula...
case class RefToken(ref : String) extends Tokens //represents a cell in the table D20 or so...
case object EndOfInputToken extends Tokens //represents the end of the token





