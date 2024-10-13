package Evaluation
import Evaluation.EvaluationTypes.EvaluationResult
case class EvaluationError(message: String) extends EvaluationResult[Nothing] {

  // Since this is an error, return None for toFloat and toInt
  override  def toInt: Option[Int] = None

  override  def toFloat: Option[Double] = None

  override def isEmpty: Boolean = false
 
}

