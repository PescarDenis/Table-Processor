package ProcessingModules

import CLIInterface.CLIConfig
import File_Reader.{CSVReader, CSVSeparator}
import Table.DefinedTabels.BaseTable
import TableParser.FileParser

import scala.io.Source
// Responsible for loading the table from a specific file, with different separators

class InputLoader(config: CLIConfig, parser: FileParser) {

  def loadTable(): BaseTable = {
    val inputSource = Source.fromFile(config.inputFile.get)
    try {
      val separator = CSVSeparator(config.inputSeparator)
      val csvReader = new CSVReader(inputSource, separator)
      val baseTable = new BaseTable(parser)
      baseTable.parse(csvReader)
      baseTable
    } finally {
      inputSource.close()
    }
  }
}


