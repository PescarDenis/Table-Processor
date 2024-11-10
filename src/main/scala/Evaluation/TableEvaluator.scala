package Evaluation

import Evaluation.EvaluationTypes._
import Table.ParseTableCells
import Table.TableEntries._
import Table.TableInterfaces.{EvaluatedTableInterface, RawTableInterface}
import ExpressionAST.EvaluationContext


class TableEvaluator(
                      context: EvaluationContext,
                      private val formulaEvaluator: FormulaEvaluator
                    ) {

  // Evaluates a single cell and stores the result
  def evaluateCellAndStore(
                            pos: ParseTableCells,
                            visited: Set[ParseTableCells],
                            rawTable: RawTableInterface[TableEntry],
                            evaluatedTable: EvaluatedTableInterface[EvaluationResult[_]]
                          ): EvaluationResult[_] = {

    rawTable.getCell(pos) match {
      case number: Number =>
        val result = IntResult(number.numberValue.getOrElse(0))
        evaluatedTable.storeEvaluatedResult(pos, result)
        result

      case _: Empty =>
        val result = EmptyResult
        evaluatedTable.storeEvaluatedResult(pos, result)
        result

      case formula: Formula =>
        // Delegate formula evaluation to FormulaEvaluator
        val result = formulaEvaluator.evaluateFormula(formula, context, visited)
        evaluatedTable.storeEvaluatedResult(pos, result)
        result

      case _ =>
        val error = EvaluationError(s"Invalid entry at $pos")
        evaluatedTable.storeEvaluatedResult(pos, error)
        error
    }
  }

  // Evaluates all cells in the table
  def evaluateAllCellsAndStoreResults(
                                       rawTable: RawTableInterface[TableEntry],
                                       evaluatedTable: EvaluatedTableInterface[EvaluationResult[_]]
                                     ): Unit = {
    rawTable.nonEmptyPositions.foreach { pos =>
      evaluateCellAndStore(pos, Set.empty, rawTable, evaluatedTable)
    }
  }
}
