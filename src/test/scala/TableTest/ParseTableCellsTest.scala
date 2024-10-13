package TableTest

import org.scalatest.funsuite.AnyFunSuite
import Table.ParseTableCells

class ParseTableCellsTest extends AnyFunSuite {
  test("test1") {
    assert(ParseTableCells.parse("12H").isEmpty)
    assert(ParseTableCells.parse("Hey").isEmpty)
    assert(ParseTableCells.parse("").isEmpty)
    assert(ParseTableCells.parse("12").isEmpty)
  }

  test("test2") {
    assert(ParseTableCells.parse("B12").contains(ParseTableCells(12, 2)))
    assert(ParseTableCells.parse("A0").isEmpty)
    assert(ParseTableCells.parse("C9").contains(ParseTableCells(9, 3)))
    assert(ParseTableCells.parse("AA201").contains(ParseTableCells(201, 27)))
  }

}
