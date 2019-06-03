package dao;

import javax.inject.{Inject, Singleton}
import models.{Beer, Drink}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

// We use a trait component here in order to share the BeersTable class with other DAO, thanks to the inheritance.
trait BeersComponent extends DrinksComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  // This class convert the database's Beers table in a object-oriented entity: the Student model.
  class BeersTable(tag: Tag) extends Table[Beer](tag, "Beers") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc) // Primary key, auto-incremented
    def drinkId = column[Long]("DRINK_ID")
    def provenance = column[String]("PROVENANCE")
    def alcool = column[Float]("ALCOOL")

    def drink = foreignKey("DRINK_FK", drinkId, drinks)(_.id)

    // Map the attributes with the model; the ID is optional.
    def * = (id.?, drinkId, provenance, alcool) <> (Beer.tupled, Beer.unapply)
  }

  lazy val beers = TableQuery[BeersTable]

}

// This class contains the object-oriented list of Beers and offers methods to query the data.
// A DatabaseConfigProvider is injected through dependency injection; it provides a Slick type bundling a database and
// driver. The class extends the Beers' query table and loads the JDBC profile configured in the application's
// configuration file.
@Singleton
class BeersDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends BeersComponent with HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  // Get the object-oriented list of Beers directly from the query table.
  val Beers = TableQuery[BeersTable]

  /** Retrieve the list of Beers */
  def list(): Future[Seq[(Drink, Beer)]] = {
    /// Beers.sortBy(s => s.drinks.name)
    val query = (for {
      beer <- beers
      drink <- beer.drink
    } yield (drink, beer)).sortBy(_._1.name)
    db.run(query.result)
  }

  /** Retrieve the names (first and last names) and the age of the Beers, whose age is inferior of the given one,
    * then sort the results by last name, then first name */
  /*def findIfAgeIsInferior(age: Int): Future[Seq[(String, String, Int)]] = {
    val query = (for {
      student <- Beers
      if student.age < age
    } yield (student.firstName, student.lastName, student.age)).sortBy(s => (s._2, s._1))

    db.run(query.result)
  }*/

  /** Retrieve a beer from the id. */
  def findById(id: Long): Future[Seq[(Beer, Drink)]] = {
    val query = for {
      beer <- beers
      if beer.id == id
      drink <- beer.drink
    } yield (beer, drink)
    db.run(query.result)
  }

  /** Insert a new beer, then return it. */
  def insert(beer: Beer): Future[Beer] = {
    val insertQuery = Beers returning Beers.map(_.id) into ((beer, id) => beer.copy(Some(id)))
    db.run(insertQuery += beer)
  }

  /** Update a beer, then return an integer that indicate if the student was found (1) or not (0). */
  def update(id: Long, beer: Beer): Future[Int] = {
    val beerToUpdate: Beer = beer.copy(Some(id))
    db.run(Beers.filter(_.id === id).update(beerToUpdate))
  }

  /** Delete a beer, then return an integer that indicate if the student was found (1) or not (0). */
  def delete(id: Long): Future[Int] =
    db.run(Beers.filter(_.id === id).delete)

  /** Archived a beer */
 /* def archived(id: Long): Future[Int] = {
    val targetRows = Beers.filter(_.id === id).map(_.isArchived)
    val actions = for {
      booleanOption <- targetRows.result.headOption
      updateActionOption = booleanOption.map(b => targetRows.update(!b))
      affected <- updateActionOption.getOrElse(DBIO.successful(0))
    } yield affected

    db.run(actions)
  }*/
}