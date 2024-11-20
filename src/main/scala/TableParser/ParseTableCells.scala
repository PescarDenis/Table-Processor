package TableParser


// Immutable data representing the location of a cell (row and column)
case class ParseTableCells(row: Int, col: Int) {
  override def toString: String = {
    f"${ParseTableCells.getColName(col)}${ParseTableCells.getRowName(row)}"
    // Construct the string using string interpolation, e.g., "A1", "B5"
  }
}

object ParseTableCells {

  val LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
  
  def parse(str: String): Option[ParseTableCells] = {
    val pattern = """^([A-Z]+)(\d+)$""".r
    str match {
      case pattern(columnStr, rowStr) =>
        val rowIndex = rowStr.toInt // Keep rowIndex 1-based directly
        
        val colIndex = getColIndex(columnStr)
        Some(ParseTableCells(rowIndex, colIndex)) // Both row and column are 1-based
      case _ => None
    }
  }

  def getColName(col: Int): String = {
    var temp = col - 1 // Shift down for 1-based indexing
    var colName = ""
    while (temp >= 0) {
      colName = s"${LETTERS.charAt(temp % LETTERS.length)}$colName"
      temp = temp / LETTERS.length - 1
    }
    colName
  }
  
   def getColIndex(colStr: String): Int = {
    var index = 0
    for (c <- colStr) {
      index = LETTERS.length * index + (c - 'A' + 1)
    }
    index 
  }
  
   def getRowName(row: Int): String = row.toString
  

}
