package Evaluation.EvaluationTypes

//make the trait a generic to be able to add whatever result we want, strings booleans...
trait EvaluationResult[T] {
  def toInt : Option[Int]
  def toFloat : Option[Double]
  def isEmpty : Boolean
}


