package ExpressionAST

import Table.TableEntries.TableEntry
import Table.TableEntries.Empty
import TableParser.ParseTableCells

//component for evaluating expressions within the context of a table
class EvaluationContext(table : Map[ParseTableCells,TableEntry] ){ ///the constructor parameter takes a map
  // where the keys are the position of each cell and the TableEntry are the data in those cells

  def getTable: Map[ParseTableCells, TableEntry] = table //getter method to access  the table

  def lookup(cell : ParseTableCells) : TableEntry ={
    table.getOrElse(cell, Empty(cell.row,cell.col)) //if the cell does not exist in a map , it returns an instance of EmptyEntry
  }

}


