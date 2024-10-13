package TableTest

import Table.TableEntries.{Empty, Formula, Number}
import org.scalatest.funsuite.AnyFunSuite

class TableEntryTest extends AnyFunSuite {

  test("Empty cell should be empty") {
    val emptyCell =  Empty(1, 1)
    assert(emptyCell.isEmpty)
    assert(emptyCell.get == "")
  }

  test("Number cell should store valid positive integers") {
    val numberCell =  Number(1, 2)
    numberCell.set("5")
    assert(!numberCell.isEmpty)
    assert(numberCell.get == "5")
  }

  test("Number cell should reject non-numeric input") {
    val numberCell =  Number(1, 2)
    assertThrows[IllegalArgumentException] {
      numberCell.set("abc") // Non-numeric input
    }
  }

  test("Number cell should reject negative numbers") {
    val numberCell =  Number(1, 2)
    assertThrows[IllegalArgumentException] {
      numberCell.set("-1") // Negative number
    }
  }

}
