package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class AboutController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def about = Action {
    Ok(views.html.about())
  }

}
