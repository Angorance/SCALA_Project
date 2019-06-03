package dao

import models.Chat
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

trait ChatComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  // This class convert the database's Chats table in a object-oriented entity: the Student model.
  class ChatTable(tag: Tag) extends Table[Chat](tag, "CHAT") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc) // Primary key, auto-incremented
    def chatId = column[String]("CHATID")

    // Map the attributes with the model; the ID is optional.
    def * = (id.?, chatId) <> (Chat.tupled, Chat.unapply)
  }
}

@Singleton
class ChatDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
    extends ChatComponent with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  // Get the object-oriented list of Chats directly from the query table.
  val chats = TableQuery[ChatTable]

  /** Retrieve the list of Chats */
  def list(): Future[Seq[Chat]] = {
    val query = chats.sortBy(s => s.chatId)
    db.run(query.result)
  }

  /** Retrieve the names (first and last names) and the age of the Chats, whose age is inferior of the given one,
    * then sort the results by last name, then first name */
  /*def findIfAgeIsInferior(age: Int): Future[Seq[(String, String, Int)]] = {
    val query = (for {
      student <- Chats
      if student.age < age
    } yield (student.firstName, student.lastName, student.age)).sortBy(s => (s._2, s._1))

    db.run(query.result)
  }*/

  /** Retrieve a student from the id. */
  def findById(id: Long): Future[Option[Chat]] =
    db.run(chats.filter(_.id === id).result.headOption)

  /** Insert a new chat, then return it. */
  def insert(Chat: Chat): Future[Chat] = {
    wipe()

    val insertQuery = chats returning chats.map(_.id) into ((Chat, id) => Chat.copy(Some(id)))
    db.run(insertQuery += Chat)
  }

  /** Deletes all chats */
  def wipe(): Future[Int] = db.run(chats.delete)
}
