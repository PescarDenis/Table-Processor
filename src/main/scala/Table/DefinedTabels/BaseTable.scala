package Table.DefinedTabels

import Evaluation.EvaluationTypes.{EvaluationResult, FloatResult, IntResult, EmptyResult}
import File_Reader.CSVReader
import Table.{ParseTableCells, TableInterface}
import Table.TableEntries.{Empty, Formula, Number, TableEntry}

//implementation of the interface for a basic table -> aka our input table
class BaseTable extends TableInterface {
  private var rows: Map[ParseTableCells, TableEntry] = Map() //store the table entries, mapped from cell positions to entries
  private var evaluatedResults: Map[ParseTableCells, EvaluationResult[_]] = Map()
  //store evaluated result of the table

  def getTable: Map[ParseTableCells, TableEntry] = rows //getter for the internal row map
  
  override def getCell(pos: ParseTableCells): TableEntry = {
    rows.getOrElse(pos, Empty(pos.row, pos.col))
  } //retrive the entry at agiven pos

  override def getRow(rowIndex: Int): Map[ParseTableCells, EvaluationResult[_]] = {
    evaluatedResults.filter { case (cell, _) => cell.row == rowIndex }
  } //get the evaluated resuluts for a specific row
  
  override def lastRow: Option[Int] = {
    if (rows.isEmpty) None else rows.keys.map(_.row).maxOption
  } //retrive the last row

  override def lastColumn: Option[Int] = {
    if (rows.isEmpty) None else rows.keys.map(_.col).maxOption
  }//retrive the last collumn

  override def nonEmptyPositions: Iterable[ParseTableCells] = rows.keys
  //return all cell positions that have some entires
  
  override def storeEvaluatedResult(pos: ParseTableCells, result: EvaluationResult[_]): Unit = {
    evaluatedResults += (pos -> result)
  }//store the evaluated results

  override def getEvaluatedResult(pos: ParseTableCells): Option[EvaluationResult[_]] = {
    evaluatedResults.get(pos)
  }//get the evaluated results

  override def getEvaluatedResultAsString(pos: ParseTableCells): String = {
    evaluatedResults.get(pos).map {
      case IntResult(value) => value.toString
      case FloatResult(value) => value.toString
      case EmptyResult => ""
      case _ => "ERROR"
    }.getOrElse(" ") //convert each result to its string implementation
  }

  //parses a File reader input into table entries, constructing rows based on cell values
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
  //parses a cell value into an appropriate table entry type based on its content
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
