package models

trait Drink {
  def id: Option[Long]
  def name: String
  def volume: Int
  def description: String
  def isArchived: Boolean
  def rankingValue: Int
  def nbRanking: Int
  def picture: String
}

case class SoftDrink(id: Option[Long], name: String, volume: Int, description: String, isArchived: Boolean = false, picture: String, rankingValue: Int, nbRanking: Int) extends Drink

case class Beer(id: Option[Long], name: String, volume: Int, description: String, isArchived: Boolean = false, picture: String, rankingValue: Int, nbRanking: Int, provenance: String, alcool: Float) extends Drink

case class BeerOfTheMonth (id: Option[Long], beerId: Long, month: Int, year: Int)

case class Storage(id: Option[Long], name: String)

case class StorageDrinks(id: Option[Long], quantity: Int, storageId: Long, drinkId: Long)

case class BlackList(id: Option[Long], mac: String, drinkId: Long)

case class Staff(id: Option[Long], pseudo: String, password: String)
