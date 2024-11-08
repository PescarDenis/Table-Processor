package Table.DefinedTables

import Table.ParseTableCells
import Table.TableEntries.{TableEntry, Empty}
import Table.TableInterfaces.{EvaluatedTableInterface, RawTableInterface}
import Evaluation.EvaluationTypes.{EvaluationResult, EmptyResult,IntResult,FloatResult,EvaluationError}


class BaseTable extends RawTableInterface[_] with EvaluatedTableInterface[EvaluationResult[_]] {

  private var rows: Map[ParseTableCells, TableEntry] = Map()
  private var evaluatedResults: Map[ParseTableCells, EvaluationResult[_]] = Map()

  // RawTableInterface methods
  override def getCell(pos: ParseTableCells): TableEntry =
    rows.getOrElse(pos, Empty(pos.row, pos.col))

  override def lastRow: Option[Int] =
    if (rows.isEmpty) None else rows.keys.map(_.row).maxOption

  override def lastColumn: Option[Int] =
    if (rows.isEmpty) None else rows.keys.map(_.col).maxOption

  override def nonEmptyPositions: Iterable[ParseTableCells] =
    rows.keys

  // EvaluatedTableInterface methods
  override def storeEvaluatedResult(pos: ParseTableCells, result: EvaluationResult[_]): Unit =
    evaluatedResults += (pos -> result)

  override def getEvaluatedResult(pos: ParseTableCells): Option[EvaluationResult[_]] =
    evaluatedResults.get(pos)

  override def getEvaluatedResultAsString(pos: ParseTableCells): String =
    evaluatedResults.get(pos).map {
      case IntResult(value) => value.toString
      case FloatResult(value) => value.toString
      case EmptyResult => ""
      case EvaluationError(message) => s"ERROR: $message"
      case _ => "UNKNOWN RESULT"
    }.getOrElse(" ")

  override def getRow(rowIndex: Int): Map[ParseTableCells, EvaluationResult[_]] =
    evaluatedResults.filter { case (cell, _) => cell.row == rowIndex }
}
