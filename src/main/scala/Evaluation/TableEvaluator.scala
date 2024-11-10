package Evaluation

import Evaluation.EvaluationTypes.*
import ExpressionAST.EvaluationContext
import Table.ParseTableCells
import Table.TableInterfaces.{EvaluatedTableInterface, RawTableInterface}
import Table.TableEntries.{Formula, TableEntry}

class TableEvaluator {

  def evaluateAllCellsAndStoreResults(
                                       rawTable: RawTableInterface[TableEntry],
                                       evaluatedTable: EvaluatedTableInterface[EvaluationResult[_]]
                                     ): Unit = {
    rawTable.nonEmptyPositions.foreach { pos =>
      val result = evaluateCell(pos, Set.empty, rawTable)
      evaluatedTable.storeEvaluatedResult(pos, result)
    }
  }

   def evaluateCell(
                            pos: ParseTableCells,
                            visited: Set[ParseTableCells],
                            rawTable: RawTableInterface[TableEntry]
                          ): EvaluationResult[_] = {
    rawTable.getCell(pos) match {
      case formula: Formula => formula.getExpression match {
        case Some(expression) => expression.evaluate(
          new EvaluationContext(rawTable, this),
          visited + pos
        )
        case None => EvaluationError("Unparsed Formula")
      }
      case _ => EmptyResult
    }
  }
}
