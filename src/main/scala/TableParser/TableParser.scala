package TableParser
import File_Reader.CSVReader
import Table.TableEntries.TableEntry
import Table.TableModel
// Basic interface to parse a File and return its Table Model 
trait TableParser {
  def parse(data: CSVReader): TableModel[TableEntry]
}
