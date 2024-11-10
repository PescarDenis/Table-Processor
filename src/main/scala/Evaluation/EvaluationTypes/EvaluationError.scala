package Evaluation.EvaluationTypes

import Evaluation.EvaluationResult

//a class to help with printing a message when the evaluation goes wrong
case class EvaluationError(message: String) extends EvaluationResult[Nothing] {
  override def toValue: Option[Nothing] = None // Empty result has no value

  override def isEmpty: Boolean = false

}

