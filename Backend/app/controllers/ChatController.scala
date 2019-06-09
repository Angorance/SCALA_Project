package controllers

import dao.ChatDAO
import javax.inject.Inject
import models.Chat
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{AbstractController, ControllerComponents}

import scalaj.http.Http

import scala.concurrent.Future
import scala.util.{Failure, Success}

class ChatController @Inject()(cc: ControllerComponents, chatDAO: ChatDAO) extends AbstractController(cc) {

  val chatForm: Form[NewChatForm] = Form(
    mapping(
      "chatId" -> text()
    )(NewChatForm.apply)(NewChatForm.unapply))

  def chatFormPost() = Action { implicit request =>
    val data = chatForm.bindFromRequest.get
    chatDAO.insert(data.chatId)
    Ok(data.chatId)
  }

  /*def index = Action { implicit request =>
    Ok(views.html.about)
  }*/

  /*def postChatID = Action {
    Ok(views.html.chat("oldCHat"))
  }*/

  /*def chat = Action {

    // TODO Fetch data
    Ok(views.html.chat(chatForm))
  }*/

  @throws(classOf[java.io.IOException])
  def needHelp() = Action { request =>
    val chatId = "-376102439" // FIXME: Should be recovered in database with DAO
    val botToken = "846315786:AAHs2htjVdkuCvVTL1wJd-2H6CU8Z21XpNM" // FIXME: Should be an env variable
    val url = "https://api.telegram.org/bot" + botToken + "/sendMessage"
    val params: Seq[(String, String)] = Seq(("chat_id", chatId), ("text", "Need Help!"))

    val res = Http(url).params(params).asString

    if (res.isSuccess) Redirect("/msgSent") else InternalServerError("Oops")
  }
}

case class NewChatForm(chatId: String)
