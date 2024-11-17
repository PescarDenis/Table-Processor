package TableParser

import ExpressionParser.ParsingServices.ExpressionParser
import File_Reader.CSVReader
import Table.TableEntries.{Empty, Formula, Number, TableEntry}
import Table.TableModel

class FileParser(parser: ExpressionParser) extends TableParser {

  override def parse(data: CSVReader): TableModel[TableEntry] = {
    val entries = data.zipWithIndex.flatMap { case (row, rowIndex) =>
      row.zipWithIndex.map { case (cellValue, colIndex) =>
        val cellPos = ParseTableCells(rowIndex + 1, colIndex + 1)
        cellPos -> parseCell(cellValue.trim, rowIndex + 1, colIndex + 1)
      }
    }.toMap

    new TableModel(entries)
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
        case _: NumberFormatException => throw new IllegalArgumentException(
          s"Invalid cell content at ($rowIndex,$colIndex): '$cellValue'. Expected a number, formula, or empty cell."
        )
      }
    }
  }
}
