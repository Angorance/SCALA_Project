package dao;

import javax.inject.{Inject, Singleton}
import models.{Drink}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

// We use a trait component here in order to share the DrinksTable class with other DAO, thanks to the inheritance.
trait DrinksComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  // This class convert the database's Drinks table in a object-oriented entity: the Student model.
  class DrinksTable(tag: Tag) extends Table[Drink](tag, "Drinks") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc) // Primary key, auto-incremented
    def name = column[String]("NAME")
    def volume = column[Int]("VOLUME")
    def description = column[String]("DESCRIPTION")
    def isArchived = column[Boolean]("ISARCHIVED")
    def rankingValue = column[Int]("RANKINGVALUE")
    def nbRanking = column[Int]("NBRANKING")
    def picture = column[String]("PICTURE")

    // Map the attributes with the model; the ID is optional.
    def * = (id.?, name, volume, description, isArchived, picture, rankingValue, nbRanking) <> (Drink.tupled, Drink.unapply)
  }

  lazy val drinks = TableQuery[DrinksTable]
}

// This class contains the object-oriented list of Drinks and offers methods to query the data.
// A DatabaseConfigProvider is injected through dependency injection; it provides a Slick type bundling a database and
// driver. The class extends the Drinks' query table and loads the JDBC profile configured in the application's
// configuration file.
@Singleton
class DrinksDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends DrinksComponent with HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._


  /** Retrieve the list of Drinks */
  def list(): Future[Seq[Drink]] = {
    val query = drinks.sortBy(s => s.name)
    db.run(query.result)
  }

  /** Retrieve the names (first and last names) and the age of the Drinks, whose age is inferior of the given one,
    * then sort the results by last name, then first name */
  /*def findIfAgeIsInferior(age: Int): Future[Seq[(String, String, Int)]] = {
    val query = (for {
      student <- Drinks
      if student.age < age
    } yield (student.firstName, student.lastName, student.age)).sortBy(s => (s._2, s._1))

    db.run(query.result)
  }*/

  /** Retrieve a Drink from the id. */
  def findById(id: Long): Future[Option[Drink]] =
    db.run(drinks.filter(_.id === id).result.headOption)

  /** Insert a new Drink, then return it. */
  def insert(Drink: Drink): Future[Drink] = {
    val insertQuery = drinks returning drinks.map(_.id) into ((Drink, id) => Drink.copy(Some(id)))
    db.run(insertQuery += Drink)
  }

  /** Update a Drink, then return an integer that indicate if the student was found (1) or not (0). */
  def update(id: Long, Drink: Drink): Future[Int] = {
    val DrinkToUpdate: Drink = Drink.copy(Some(id))
    db.run(drinks.filter(_.id === id).update(DrinkToUpdate))
  }

  /** Delete a Drink, then return an integer that indicate if the student was found (1) or not (0). */
  def delete(id: Long): Future[Int] =
    db.run(drinks.filter(_.id === id).delete)

  /** Archived a Drink */
  def archived(id: Long): Future[Int] = {
    val targetRows = drinks.filter(_.id === id).map(_.isArchived)
    val actions = for {
      booleanOption <- targetRows.result.headOption
      updateActionOption = booleanOption.map(b => targetRows.update(!b))
      affected <- updateActionOption.getOrElse(DBIO.successful(0))
    } yield affected

    db.run(actions)
  }
}