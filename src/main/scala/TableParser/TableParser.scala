package TableParser
import File_Reader.CSVReader
import Table.TableEntries.TableEntry
trait TableParser {
  def parse(data: CSVReader): Map[ParseTableCells, TableEntry]
}
