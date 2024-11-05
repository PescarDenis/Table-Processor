package TableTest

import org.scalatest.funsuite.AnyFunSuite
import Table.ParseTableCells

class ParseTableCellsTest extends AnyFunSuite {
  test("test invalid inputs") {
    assert(ParseTableCells.parse("12H").isEmpty)
    assert(ParseTableCells.parse("Hey").isEmpty)
    assert(ParseTableCells.parse("").isEmpty)
    assert(ParseTableCells.parse("12").isEmpty)
  }

  test("test valid basic inputs") {
    assert(ParseTableCells.parse("B12").contains(ParseTableCells(12, 2)))
    assert(ParseTableCells.parse("A0").isEmpty)
    assert(ParseTableCells.parse("C9").contains(ParseTableCells(9, 3)))
    assert(ParseTableCells.parse("AA201").contains(ParseTableCells(201, 27)))
  }
  test("test column names") {
    assert(ParseTableCells.parse("A3").contains(ParseTableCells(3, 1)))
    assert(ParseTableCells.parse("Z23").contains(ParseTableCells(23, 26)))
    assert(ParseTableCells.parse("AA1").contains(ParseTableCells(1, 27)))
    assert(ParseTableCells.parse("AZ1").contains(ParseTableCells(1, 52)))
    assert(ParseTableCells.parse("BA1").contains(ParseTableCells(1, 53)))
  }

}
