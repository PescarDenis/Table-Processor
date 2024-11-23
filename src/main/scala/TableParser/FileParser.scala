package TableParser

import ExpressionParser.ParsingServices.ExpressionParser
import File_Reader.CSVReader
import Table.TableEntries.{Empty, Formula, Number, TableEntry}
import Table.TableModel

//Knows how to parse a file
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


  // Helper method to parse each cell based on its entry formula,number or empty cell
    private def parseCell(cellValue: String, rowIndex: Int, colIndex: Int): TableEntry = {
    if (cellValue.trim.isEmpty) {
      Empty(rowIndex, colIndex)
    } else if (cellValue.startsWith("=") || (cellValue.matches(".*[+*/].*") && !cellValue.matches("-?\\d+"))) { //It checks if it is a formula based on : starts with = or, there is
      //a defined operator of ours that is in that cell for example 5*2 is also treated as a formula, and ensures that we don't mistake the numbers like -22 as formulas instead of numbers
      val formulaEntry =  Formula(rowIndex, colIndex,parser)
      formulaEntry.set(cellValue)
      formulaEntry
    } else {
      try {
        val numberEntry = Number(rowIndex, colIndex)
        numberEntry.set(cellValue)
        numberEntry
      } catch { //If we try to parse some unknown entry throw an error
        case _: NumberFormatException => throw new NumberFormatException(
          s"Invalid cell content at ($rowIndex,$colIndex): '$cellValue'. Expected an integer positive number, formula, or empty cell."
        )
      }
    }
  }
}
