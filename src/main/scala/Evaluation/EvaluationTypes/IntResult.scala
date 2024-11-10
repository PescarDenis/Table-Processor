package Evaluation.EvaluationTypes

import Evaluation.EvaluationResult

//simple case class to store the int evaluated results
case class IntResult(value: Int) extends EvaluationResult[Int] {
  override def toValue: Option[Int] = Some(value)    // Provides the Int value
  override def isEmpty: Boolean = false              // IntResult is never empty
}
