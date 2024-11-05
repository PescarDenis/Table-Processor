package Table.TableEntries

//knows that the table entry is a number
case class Number(row : Int, col : Int) extends TableEntry(row , col) {

  var numberValue : Option[Int] = None ///store either a float or an integer

  override def get: String = numberValue.map(_.toString).getOrElse("") //return value as string or empty if None

  override def set(value: String): Unit = {
    //try to parse the value as an integer
    val intValue = value.toIntOption.filter(_ >= 0)
      .getOrElse(throw new IllegalArgumentException("Only positive integers are allowed"))

    numberValue = Some(intValue) //set the value once it's valid
  }

  override def isEmpty: Boolean = numberValue.isEmpty //we have a number so it is not empty

}
