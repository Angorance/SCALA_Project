package controllers

import dao.ChatDAO
import javax.inject.Inject
import models.Chat
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{AbstractController, ControllerComponents}

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
}

case class NewChatForm(chatId: String)
