package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

/**
  * Controlleur répondant aux requête pour la connection
  * @param cc
  * Note: La gestion des session et de la connection n'est pas encore implémenté
  */
@Singleton
class LoginController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def login = Action {
    Ok(views.html.login())
  }

}
