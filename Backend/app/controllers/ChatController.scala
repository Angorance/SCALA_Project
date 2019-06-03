package controllers

import dao.ChatDAO
import javax.inject.Inject
import models.Chat
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms.text
import play.api.mvc.{AbstractController, ControllerComponents}

class ChatController @Inject()(cc: ControllerComponents, chatDAO: ChatDAO) extends AbstractController(cc) {

  /*val chatForm = Form(
    mapping(
      "chatId" -> text())(Chat.apply)(Chat.unapply))*/
}
