package Filters

//base interface for filters

trait TableFilter {
  def matches(row: Row): Boolean
}
