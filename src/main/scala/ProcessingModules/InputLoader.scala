package ProcessingModules

import CLIInterface.CLIConfig
import File_Reader.{CSVReader, CSVSeparator}
import Table.DefinedTabels.BaseTable
import TableParser.TableParser
import scala.io.Source
//responsible for loading the table from a specific file, with different separators

// Responsible for loading the input file and delegating parsing
class InputLoader(config: CLIConfig, tableParser: TableParser) {

  def loadTable(): BaseTable = {
    val inputSource = Source.fromFile(config.inputFile.get)
    val separator = CSVSeparator(config.inputSeparator)
    val csvReader = new CSVReader(inputSource, separator)
    val table = tableParser.parse(csvReader) // Delegate parsing to TableParser
    inputSource.close()
    table
  }
}

