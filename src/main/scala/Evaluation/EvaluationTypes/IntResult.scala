package Evaluation.EvaluationTypes

case class IntResult(value: Int) extends EvaluationResult[Int] {
  override def toInt: Option[Int] = Some(value)
  override def toFloat: Option[Double] = Some(value.toDouble)
  override def isEmpty: Boolean = false
}
