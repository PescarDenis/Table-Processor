package Table.DefinedTabels

import Table.{ParseTableCells, TableInterface}
import Evaluation.EvaluationTypes.{EmptyResult, EvaluationResult}

class TableRange(table: TableInterface) {

  def getRange(from: ParseTableCells, to: ParseTableCells): Map[ParseTableCells, EvaluationResult[_]] = {
    val startRow = from.row min to.row
    val endRow = from.row max to.row
    val startCol = from.col min to.col
    val endCol = from.col max to.col

    (for {
      row <- startRow to endRow
      col <- startCol to endCol
      cellPos = ParseTableCells(row, col)
      result <- table.getEvaluatedResult(cellPos)
    } yield cellPos -> result).toMap
  }

  def getDefaultRange: Map[ParseTableCells, EvaluationResult[_]] = {
    val nonEmptyPositions = table.nonEmptyPositions.toSeq
    val maxRow = nonEmptyPositions.map(_.row).maxOption.getOrElse(0)
    val maxCol = nonEmptyPositions.map(_.col).maxOption.getOrElse(0)

    (for {
      row <- 1 to maxRow
      col <- 1 to maxCol
      cellPos = ParseTableCells(row, col)
      result <- table.getEvaluatedResult(cellPos) if !result.isInstanceOf[EmptyResult.type]
    } yield cellPos -> result).toMap
  }

  def printRange(cells: Map[ParseTableCells, EvaluationResult[_]]): Unit = {
    val rows = cells.keys.map(_.row).maxOption.getOrElse(0)
    val cols = cells.keys.map(_.col).maxOption.getOrElse(0)

    for (r <- 1 to rows) {
      for (c <- 1 to cols) {
        val cellPos = ParseTableCells(r, c)
        val value = table.getEvaluatedResultAsString(cellPos)
        print(s"$value\t")
      }
      println()
    }
  }
}
