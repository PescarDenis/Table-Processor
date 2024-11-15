package ExpressionAST

import Table.TableEntries.TableEntry
import Table.TableEntries.Empty
import TableParser.ParseTableCells
import Table.TableInterface
//component for evaluating expressions within the context of a table
class EvaluationContext(table : TableInterface ){ ///the constructor parameter takes a map
  // where the keys are the position of each cell and the TableEntry are the data in those cells

  def lookup(cell: ParseTableCells): TableEntry = {
    table.getCell(cell) match {
      case entry: TableEntry => entry
    }
  }

}


