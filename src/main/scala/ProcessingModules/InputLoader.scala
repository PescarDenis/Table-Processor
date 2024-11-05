package ProcessingModules

import CLIInterface.CLIConfig
import File_Reader.{CSVReader, CSVSeparator}
import Table.DefinedTabels.BaseTable

import scala.io.Source
//responsible for loading the table from a specific file, with different separators
class InputLoader(config: CLIConfig) {
  def loadTable(): BaseTable = {
    val inputSource = Source.fromFile(config.inputFile.get)
    val separator = CSVSeparator(config.inputSeparator)
    val csvReader = new CSVReader(inputSource, separator)
    val table = new BaseTable().parse(csvReader).asInstanceOf[BaseTable] //it also parses the data into a BaseTable so we can work with it
    inputSource.close()
    table
  }
}
