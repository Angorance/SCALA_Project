package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

/**
  * Controlleur géréant les requêtes concernant la page du à propos.
  * @param cc
  */
@Singleton
class AboutController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
    * Retourne la page contenant les données du à propos.
    * @return
    */
  def about = Action {
    Ok(views.html.about())
  }

}
