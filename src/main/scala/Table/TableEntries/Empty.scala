package Table.TableEntries

case class Empty(row : Int, col : Int) extends TableEntry(row , col) {
  override def isEmpty: Boolean = true

  override def get: String = ""

  override def set(value: String): Unit = {}
}
