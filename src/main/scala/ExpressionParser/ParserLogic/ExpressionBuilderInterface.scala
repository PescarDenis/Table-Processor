package ExpressionParser.ParserLogic
import ExpressionAST._
//an interface for building the expression from the lexer
trait ExpressionBuilderInterface[T] {
  def buildConstant(value: String): Expression[T] //builds a constant expression
  def buildRef(value: String): Expression[T] //builds refference expression
  def buildBinaryOperation(left: Expression[T], right: Expression[T], op: String): Expression[T]
  //builds the specified binary operation between the two given expressions
}
