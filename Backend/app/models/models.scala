package models

trait DrinkTrait {
  val id: Option[Long]
  val drinkId: Long
}

case class Drink(id: Option[Long], name: String, volume: Int, description: String, isArchived: Boolean, rankingValue: Int, nbRanking: Int, picture: String)

case class SoftDrink(id: Option[Long], drinkId: Long) extends DrinkTrait

case class Beer(id: Option[Long], drinkId: Long, provenance: String, alcool: Float) extends DrinkTrait

case class BeerOfTheMonth (id: Option[Long], beerId: Long, month: Int, year: Int)

case class Storage(id: Option[Long], name: String)

case class StorageDrinks(id: Option[Long], quantity: Int, storageId: Long, drinkId: Long)

case class BlackList(id: Option[Long], mac: String, drinkId: Long)

case class Staff(id: Option[Long], pseudo: String, password: String)

case class Chat(id:Option[Long], chatId: String)