package Table.DefinedTabels

import Table.{ParseTableCells, TableInterface}
import Evaluation.EvaluationTypes.{EmptyResult, EvaluationResult}
//retrieves a specific cell range
class TableRange(table: TableInterface) {

  //getter for the range ->returns a map of evaluated cells
  def getRange(from: ParseTableCells, to: ParseTableCells): Map[ParseTableCells, EvaluationResult[_]] = {
    //determine the minimum and the maximum rows and columns for the specific range
    val startRow = from.row min to.row
    val endRow = from.row max to.row
    val startCol = from.col min to.col
    val endCol = from.col max to.col

    (for {
      row <- startRow to endRow
      col <- startCol to endCol
      cellPos = ParseTableCells(row, col)  //create the cell position
      result <- table.getEvaluatedResult(cellPos) //get the evaluation result for the cell
    } yield cellPos -> result).toMap //map each cell pos to its result
  }
  //returns a map of cell positions to evaluation results for all non-empty cells
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

  //print the evaluation results for a range of cells(used for testing purposes)
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
