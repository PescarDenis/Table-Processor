package Evaluation.EvaluationTypes

import Evaluation.EvaluationResult

//simple case class to store the floated evaluated results
case class FloatResult(value: Double) extends EvaluationResult[Double] {
  override def toValue: Option[Double] = Some(value) // Provides the Double value
  override def isEmpty: Boolean = false              // FloatResult is never empty
}
