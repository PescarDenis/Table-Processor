package PrettyPrint

import File_Reader.CSVSeparator

//Registry object to manage PrettyPrinter instances for different formats
object PrettyPrinterRegistry {
  //a map that associates format strings (e.g., "csv", "md") with functions that create PrettyPrinters.
  private var printers: Map[String, CSVSeparator => PrettyPrinter] = Map()

  //registers a PrettyPrinter creator function for a specific format
  def register(format: String, creator: CSVSeparator => PrettyPrinter): Unit = {
    printers += (format.toLowerCase -> creator)
  }

  //retrieves a PrettyPrinter for the specified format
  def getPrinter(format: String, separator: CSVSeparator): PrettyPrinter = {
    printers.getOrElse(format.toLowerCase,
      throw new IllegalArgumentException(s"Unknown format: $format")
    )(separator)
  }
}
