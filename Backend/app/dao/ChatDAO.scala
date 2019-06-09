package dao

import models.Chat
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

/**
  * DAO intéragissant avec la table Chat
  */
trait ChatComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  // This class convert the database's Chats table in a object-oriented entity: the Student model.
  class ChatTable(tag: Tag) extends Table[Chat](tag, "Chat") {
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
  def list() = {
    val query = chats.map(c => c.chatId)
    db.run(query.take(1).result.head)
  }

  /** Retrieve a student from the id. */
  def findById(id: Long): Future[Option[Chat]] =
    db.run(chats.filter(_.id === id).result.headOption)

  /** Insert a new chat, then return it. */
  def insert(chatId: String): Future[Chat] = {
    wipe()

    val insertQuery = chats.map(c => c.chatId) returning chats.map(_.id) into ((chatId, id) => Chat(Option(id), chatId))

    db.run(insertQuery += chatId)
  }

  /** Deletes all chats */
  def wipe(): Future[Int] = db.run(chats.delete)
}
