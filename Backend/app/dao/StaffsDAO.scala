package dao

import javax.inject.{Inject, Singleton}
import models.Staff
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

// We use a trait component here in order to share the StaffsTable class with other DAO, thanks to the inheritance.
trait StaffsComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  // This class convert the database's Staffs table in a object-oriented entity: the Student model.
  class StaffsTable(tag: Tag) extends Table[Staff](tag, "Staffs") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc) // Primary key, auto-incremented
    def pseudo = column[String]("PSEUDO")
    def password = column[String]("PASSWORD")

    // Map the attributes with the model; the ID is optional.
    def * = (id.?, pseudo, password) <> (Staff.tupled, Staff.unapply)
  }
}

// This class contains the object-oriented list of Staffs and offers methods to query the data.
// A DatabaseConfigProvider is injected through dependency injection; it provides a Slick type bundling a database and
// driver. The class extends the Staffs' query table and loads the JDBC profile configured in the application's
// configuration file.
@Singleton
class StaffsDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends StaffsComponent with HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  // Get the object-oriented list of Staffs directly from the query table.
  val Staffs = TableQuery[StaffsTable]

  /** Retrieve the list of Staffs */
  def list(): Future[Seq[Staff]] = {
    val query = Staffs.sortBy(s => s.pseudo)
    db.run(query.result)
  }

  /** Retrieve the names (first and last names) and the age of the Staffs, whose age is inferior of the given one,
    * then sort the results by last name, then first name */
  /*def findIfAgeIsInferior(age: Int): Future[Seq[(String, String, Int)]] = {
    val query = (for {
      student <- Staffs
      if student.age < age
    } yield (student.firstName, student.lastName, student.age)).sortBy(s => (s._2, s._1))

    db.run(query.result)
  }*/

  /** Retrieve a student from the id. */
  def findById(id: Long): Future[Option[Staff]] =
    db.run(Staffs.filter(_.id === id).result.headOption)

  def findByPseudo(pseudo: String) : Future[Option[Staff]] =
    db.run(Staffs.filter(_.pseudo === pseudo).result.headOption)

  /** Insert a new student, then return it. */
  def insert(Staff: Staff): Future[Staff] = {
    val insertQuery = Staffs returning Staffs.map(_.id) into ((Staff, id) => Staff.copy(Some(id)))
    db.run(insertQuery += Staff)
  }

  /** Update a student, then return an integer that indicate if the student was found (1) or not (0). */
  def update(id: Long, Staff: Staff): Future[Int] = {
    val studentToUpdate: Staff = Staff.copy(Some(id))
    db.run(Staffs.filter(_.id === id).update(studentToUpdate))
  }

  /** Delete a student, then return an integer that indicate if the student was found (1) or not (0). */
  def delete(id: Long): Future[Int] =
    db.run(Staffs.filter(_.id === id).delete)
}