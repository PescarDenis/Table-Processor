package TableParser
import File_Reader.CSVReader
import Table.TableEntries.TableEntry
import Table.TableModel
trait TableParser {
  def parse(data: CSVReader): TableModel[TableEntry]
}
