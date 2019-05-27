package dao;

import javax.inject.{Inject, Singleton}
import models.SoftDrink
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

// We use a trait component here in order to share the SoftDrinksTable class with other DAO, thanks to the inheritance.
trait SoftDrinksComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  // This class convert the database's SoftDrinks table in a object-oriented entity: the Student model.
  class SoftDrinksTable(tag: Tag) extends Table[SoftDrink](tag, "SoftDrinks") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc) // Primary key, auto-incremented
    def name = column[String]("NAME")
    def volume = column[Int]("VOLUME")
    def description = column[String]("DESCRIPTION")
    def isArchived = column[Boolean]("ISARCHIVED")
    def rankingValue = column[Int]("RANKINGVALUE")
    def nbRanking = column[Int]("NBRANKING")
    def picture = column[String]("PICTURE")

    // Map the attributes with the model; the ID is optional.
    def * = (id.?, name, volume, description, isArchived, picture, rankingValue, nbRanking) <> (SoftDrink.tupled, SoftDrink.unapply)
  }
}

// This class contains the object-oriented list of SoftDrinks and offers methods to query the data.
// A DatabaseConfigProvider is injected through dependency injection; it provides a Slick type bundling a database and
// driver. The class extends the SoftDrinks' query table and loads the JDBC profile configured in the application's
// configuration file.
@Singleton
class SoftDrinksDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends SoftDrinksComponent with HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  // Get the object-oriented list of SoftDrinks directly from the query table.
  val SoftDrinks = TableQuery[SoftDrinksTable]

  /** Retrieve the list of SoftDrinks */
  def list(): Future[Seq[SoftDrink]] = {
    val query = SoftDrinks.sortBy(s => s.name)
    db.run(query.result)
  }

  /** Retrieve the names (first and last names) and the age of the SoftDrinks, whose age is inferior of the given one,
    * then sort the results by last name, then first name */
  /*def findIfAgeIsInferior(age: Int): Future[Seq[(String, String, Int)]] = {
    val query = (for {
      student <- SoftDrinks
      if student.age < age
    } yield (student.firstName, student.lastName, student.age)).sortBy(s => (s._2, s._1))

    db.run(query.result)
  }*/

  /** Retrieve a SoftDrink from the id. */
  def findById(id: Long): Future[Option[SoftDrink]] =
    db.run(SoftDrinks.filter(_.id === id).result.headOption)

  /** Insert a new SoftDrink, then return it. */
  def insert(SoftDrink: SoftDrink): Future[SoftDrink] = {
    val insertQuery = SoftDrinks returning SoftDrinks.map(_.id) into ((SoftDrink, id) => SoftDrink.copy(Some(id)))
    db.run(insertQuery += SoftDrink)
  }

  /** Update a SoftDrink, then return an integer that indicate if the student was found (1) or not (0). */
  def update(id: Long, SoftDrink: SoftDrink): Future[Int] = {
    val softDrinkToUpdate: SoftDrink = SoftDrink.copy(Some(id))
    db.run(SoftDrinks.filter(_.id === id).update(softDrinkToUpdate))
  }

  /** Delete a SoftDrink, then return an integer that indicate if the student was found (1) or not (0). */
  def delete(id: Long): Future[Int] =
    db.run(SoftDrinks.filter(_.id === id).delete)

  /** Archived a SoftDrink */
  def archived(id: Long): Future[Int] = {
    val targetRows = SoftDrinks.filter(_.id === id).map(_.isArchived)
    val actions = for {
      booleanOption <- targetRows.result.headOption
      updateActionOption = booleanOption.map(b => targetRows.update(!b))
      affected <- updateActionOption.getOrElse(DBIO.successful(0))
    } yield affected

    db.run(actions)
  }
}