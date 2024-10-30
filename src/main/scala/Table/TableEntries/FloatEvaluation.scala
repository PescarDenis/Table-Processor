package Table.TableEntries

///in case in our evaluation we get a float value make antother table entry so then we can access all the features fo the Table
//this class will only be used after evaluation, we don't care about it 
case class FloatEvaluation (row : Int, col : Int) extends TableEntry(row, col) {
  var floatValue: Option[Double] = None ///store a float after we evaluate it

  override def get: String = floatValue.map(_.toString).getOrElse("") // Return value as string or empty if None

  override def set(value: String): Unit = {
    // Try to parse the value as an integer
    val intValue = value.toFloatOption.filter(_ >= 0)
      .getOrElse(throw new IllegalArgumentException("Only positive floats after evaluation are allowed"))

    floatValue = Some(intValue) // Set the value once it's valid
  }

  override def isEmpty: Boolean = floatValue.isEmpty //we have a number so it is not empty
}
