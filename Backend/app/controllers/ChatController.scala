package controllers

import dao.ChatDAO
import javax.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}

class ChatController @Inject()(cc: ControllerComponents, chatDAO: ChatDAO) extends AbstractController(cc) {

  /*val chatForm = Form(
    mapping(
      "chatId" -> text())(Chat.apply)(Chat.unapply))*/

  def postChatID = Action {
    Ok(views.html.chat("oldCHat"))
  }

  def chat = Action {

    // TODO Fetch data
    Ok(views.html.chat("oldCHat"))
  }
}
