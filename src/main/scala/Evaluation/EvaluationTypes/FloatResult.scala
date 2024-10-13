package Evaluation.EvaluationTypes

case class FloatResult(value: Double) extends EvaluationResult[Double] {
  override def toInt: Option[Int] = Some(value.toInt)
  override def toFloat: Option[Double] = Some(value)
  override def isEmpty: Boolean = false
}
