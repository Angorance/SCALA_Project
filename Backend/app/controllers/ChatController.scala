package controllers

import dao.ChatDAO
import javax.inject.Inject
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{AbstractController, ControllerComponents, MessagesActionBuilder}
import scalaj.http.Http

import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

/**
  * Controlleur gérant les intéractions avec Telegram
  * @param messagesAction
  * @param cc
  * @param chatDAO
  */
class ChatController @Inject()(messagesAction: MessagesActionBuilder, cc: ControllerComponents, chatDAO: ChatDAO) extends AbstractController(cc) {

  val chatForm: Form[NewChatForm] = Form(
    mapping(
      "chatId" -> text()
    )(NewChatForm.apply)(NewChatForm.unapply))

  def chatFormPost() = Action.async { implicit request =>
    val data = chatForm.bindFromRequest.get

    chatDAO.insert(data.chatId).map {res => Redirect("/chatMAJ")}
  }

  def index = messagesAction { implicit request =>
    Ok(views.html.chat(chatForm))
  }

  @throws(classOf[java.io.IOException])
  def needHelp() = Action.async { request =>
    val botToken = "846315786:AAHs2htjVdkuCvVTL1wJd-2H6CU8Z21XpNM" // FIXME: Should be an env variable
    val url = "https://api.telegram.org/bot" + botToken + "/sendMessage"

    chatDAO.list().map {
      chatId => {
        val params: Seq[(String, String)] = Seq(("chat_id", chatId), ("text", "Need Help!"))
        val res = Http(url).params(params).asString

        if (res.isSuccess) Redirect("/msgSent") else InternalServerError("Oops")
      }
    }
  }
}

case class NewChatForm(chatId: String)
