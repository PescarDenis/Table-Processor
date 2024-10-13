package File_Reader


///build a trait instead of an abstract class in order
///to define the base methods( abstract ones) for the
///file reading
trait CSVIterator extends Iterator[List[String]] {
  override def hasNext: Boolean
  override def next(): List[String]
}
