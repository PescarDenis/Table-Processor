package TableParser

import ExpressionParser.ParsingServices.ExpressionParser
import File_Reader.CSVReader
import Table.TableEntries.{Empty, Formula, Number, TableEntry}

class FileParser(parser: ExpressionParser) extends TableParser {

  override def parse(data: CSVReader): Map[ParseTableCells, TableEntry] = {
    data.zipWithIndex.flatMap { case (row, rowIndex) =>
      row.zipWithIndex.map { case (cellValue, colIndex) =>
        val cellPos = ParseTableCells(rowIndex + 1, colIndex + 1)
        val entry = parseCell(cellValue, rowIndex + 1, colIndex + 1)
        cellPos -> entry
      }
    }.toMap
  }

  private def parseCell(cellValue: String, rowIndex: Int, colIndex: Int): TableEntry = {
    if (cellValue.trim.isEmpty) {
      Empty(rowIndex, colIndex)
    } else if (cellValue.startsWith("=") || cellValue.matches(".*[+\\-*/].*")) {
      val formulaEntry =  Formula(rowIndex, colIndex,parser)
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
