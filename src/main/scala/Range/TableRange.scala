package Range


import TableParser.ParseTableCells

// Provides functionality for range selection in a table
 class TableRange {

  // Returns a list of ParseTableCells within the specified range
  def getRange(from: ParseTableCells, to: ParseTableCells): List[ParseTableCells] = {
    val startRow = from.row min to.row
    val endRow = from.row max to.row
    val startCol = from.col min to.col
    val endCol = from.col max to.col

    for {
      row <- startRow to endRow
      col <- startCol to endCol
    } yield ParseTableCells(row, col)
  }.toList

  // Calculates the default range based on non-empty cells
  def getDefaultRange(nonEmptyPositions: Iterable[ParseTableCells]): List[ParseTableCells] = {
    if (nonEmptyPositions.isEmpty) {
      List.empty
    } else {
      val maxRow = nonEmptyPositions.map(_.row).max
      val maxCol = nonEmptyPositions.map(_.col).max
      val minRow = nonEmptyPositions.map(_.row).min
      val minCol = nonEmptyPositions.map(_.col).min

      for {
        row <- minRow to maxRow
        col <- minCol to maxCol
      } yield ParseTableCells(row, col)
    }.toList
  }
}
