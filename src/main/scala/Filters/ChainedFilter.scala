package Filters


case class ChainedFilter(filters: List[TableFilter]) extends TableFilter {

  override def matches(row: Row): Boolean = {
    filters.forall(_.matches(row))
  }
}
