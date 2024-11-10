package Evaluation

//make the trait a generic to be able to add whatever result we want, strings booleans...
trait EvaluationResult[+T] {
  def toValue : Option[T] //to define the evaluation based on its type, either int or float
  def isEmpty : Boolean //to define an empty cell
}


