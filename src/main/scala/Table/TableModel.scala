package Table

import TableParser.ParseTableCells

// A generic table model, I defined it as a Map of Cells and some value T.
// This can be extensible to every representation of a table like A Seq,list or whatever 
class TableModel[T](private val data: Map[ParseTableCells, T]) {

  // Get value for a specific cell
  def getCell(pos: ParseTableCells): Option[T] = data.get(pos)

  // Update a cell value, returning a new TableModel
  def updateCell(pos: ParseTableCells, value: T): TableModel[T] = {
    new TableModel(data.updated(pos, value))
  }

  // Get all entries as a map
  def toMap: Map[ParseTableCells, T] = data

  //was supposed to be for all the non empty value, but actually just returns all of the values in the table 
  def nonEmptyPositions: Iterable[ParseTableCells] = data.keys

  // Iterator for traversing the data
  def iterator: Iterator[(ParseTableCells, T)] = data.iterator
}
