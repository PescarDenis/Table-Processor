package Table.TableEntries
import ExpressionAST.EvaluationContext
///Base class of a table Entry which defines 3 methods, set and get and IsEmpty
abstract class TableEntry(row : Int, col : Int){
  def get : String
  def set(value : String) : Unit
  def isEmpty : Boolean
}
