package Evaluation.EvaluationTypes

import Evaluation.EvaluationResult

case object EmptyResult extends EvaluationResult[Nothing] {
  override def toValue: Option[Nothing] = None       // Empty result has no value
  override def isEmpty: Boolean = true               // Always empty
}
