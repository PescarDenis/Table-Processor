package Evaluation.EvaluationTypes

case object EmptyResult extends EvaluationResult[Nothing] {
  override def toInt: Option[Int] = None
  override def toFloat: Option[Double] = None
  override def isEmpty: Boolean = true // EmptyResult is empty
}
