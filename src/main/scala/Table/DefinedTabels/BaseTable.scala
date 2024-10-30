package Table.DefinedTabels

import Evaluation.EvaluationTypes.{EvaluationResult, FloatResult, IntResult, EmptyResult}
import File_Reader.CSVReader
import Table.{ParseTableCells, TableInterface}
import Table.TableEntries.{Empty, Formula, Number, TableEntry}

class BaseTable extends TableInterface {
  private var rows: Map[ParseTableCells, TableEntry] = Map()
  private var evaluatedResults: Map[ParseTableCells, EvaluationResult[_]] = Map()

  override def getCell(pos: ParseTableCells): TableEntry = {
    rows.getOrElse(pos, Empty(pos.row, pos.col))
  }

  override def getRow(rowIndex: Int): Map[ParseTableCells, EvaluationResult[_]] = {
    evaluatedResults.filter { case (cell, _) => cell.row == rowIndex }
  }
  
  override def lastRow: Option[Int] = {
    if (rows.isEmpty) None else rows.keys.map(_.row).maxOption
  }

  override def lastColumn: Option[Int] = {
    if (rows.isEmpty) None else rows.keys.map(_.col).maxOption
  }

  override def nonEmptyPositions: Iterable[ParseTableCells] = rows.keys
  
  override def storeEvaluatedResult(pos: ParseTableCells, result: EvaluationResult[_]): Unit = {
    evaluatedResults += (pos -> result)
  }

  override def getEvaluatedResult(pos: ParseTableCells): Option[EvaluationResult[_]] = {
    evaluatedResults.get(pos)
  }

  override def getEvaluatedResultAsString(pos: ParseTableCells): String = {
    evaluatedResults.get(pos).map {
      case IntResult(value) => value.toString
      case FloatResult(value) => value.toString
      case EmptyResult => " "
      case _ => "Error"
    }.getOrElse(" ")
  }
  
  override def parse(csvReader: CSVReader): TableInterface = {
    rows = csvReader.zipWithIndex.flatMap { case (row, rowIndex) =>
      row.zipWithIndex.map { case (cellValue, colIndex) =>
        val cellPos = ParseTableCells(rowIndex + 1, colIndex + 1)
        val entry = parseCell(cellValue, rowIndex + 1, colIndex + 1)
        cellPos -> entry
      }
    }.toMap
    this
  }

  private def parseCell(cellValue: String, rowIndex: Int, colIndex: Int): TableEntry = {
    if (cellValue.trim.isEmpty) {
      Empty(rowIndex, colIndex)
    } else if (cellValue.startsWith("=") || cellValue.matches(".*[+\\-*/].*")) {
      val formulaEntry = new Formula(rowIndex, colIndex)
      formulaEntry.set(cellValue)
      formulaEntry
    } else {
      try {
        val numberEntry = Number(rowIndex, colIndex)
        numberEntry.set(cellValue)
        numberEntry
      } catch {
        case _: NumberFormatException => Empty(rowIndex, colIndex)
      }
    }
  }
}
