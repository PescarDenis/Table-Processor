package File_Reader
import scala.io.Source

// A mock CSVReader for testing purposes, providing predefined rows of data
class MockCSVReader(data: List[List[String]]) extends CSVReader(Source.fromString(""),  CSVSeparator(",")) {
  private val mockData = data.iterator

  // Override next() to return the predefined data instead of reading from a file
  override def hasNext: Boolean = mockData.hasNext

  override def next(): List[String] = mockData.next()
}
