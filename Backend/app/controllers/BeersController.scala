package controllers

import dao._
import javax.inject.{Inject, Singleton}
import models.{Beer, Drink}
import play.api.libs.json._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n._
import play.api.libs.json.Json
import play.api.mvc._
import play.api.data.format.Formats._


import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class BeersController @Inject()(cc: MessagesControllerComponents, BeersDAO: BeersDAO)extends MessagesAbstractController(cc) {

  val form = Form(
    mapping(
      "name" -> text,
      "volume" -> number,
      "description" -> text,
      "provenance" -> text,
      "picture" -> text,
      "price" -> number,
      "alcool" -> bigDecimal
    )(CreateBeerForm.apply)(CreateBeerForm.unapply)
  )
  // Convert a Beer-model object into a JsValue representation, which means that we serialize it into JSON.
  /*implicit val BeerToJson: Writes[Beer] = (
    (JsPath \ "id").write[Option[Long]] and
      (JsPath \ "name").write[String] and
      (JsPath \ "volume").write[Int] and
      (JsPath \ "description").write[String] and
      (JsPath \ "isArchived").write[Boolean] and
      (JsPath \ "provenance").write[String] and
      (JsPath \ "alcool").write[Float]
    // Use the default 'unapply' method (which acts like a reverted constructor) of the Beer case class if order to get
    // back the Beer object's arguments and pass them to the JsValue.
    )(unlift(Beer.unapply))

  // Convert a JsValue representation into a Beer-model object, which means that we deserialize the JSON.
  implicit val jsonToBeer: Reads[Beer] = (
    // In order to be valid, the Beer must have first and last names that are 2 characters long at least, as well as
    // an age that is greater than 0.
    (JsPath \ "id").readNullable[Long] and
      (JsPath \ "name").read[String](minLength[String](2)) and
      (JsPath \ "volume").read[Int](min(0)) and
      (JsPath \ "description").read[String](minLength[String](2)) and
      (JsPath \ "isArchived").read[Boolean] and
      (JsPath \ "provenance").read[String](minLength[String](2)) and
      (JsPath \ "alcool").read[Float](min(0f))
    // Use the default 'apply' method (which acts like a constructor) of the Beer case class with the JsValue in order
    // to construct a Beer object from it.
    )(Beer.apply _)
  */
  /**
    * This helper parses and validates JSON using the implicit `jsonToBeer` above, returning errors if the parsed
    * json fails validation.
    */
 /* def validateJson[A : Reads] = parse.json.validate(
    _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )*/

  /**
    * Get the list of all existing Beers, then return it.
    * The Action.async is used because the request is asynchronous.
    */
  /*def getBeers = Action.async {
    val BeersList = BeersDAO.list()
    BeersList map (s => Ok(Json.toJson(s)))
  }

  */

  def getNewBeer = Action {
    Ok(views.html.newBeer(form))
  }

  /*

  /**
    * Get the Beer identified by the given ID, then return it as JSON.
    */
  def getBeer(beerId: Long) = Action.async {
    val optionalBeer = BeersDAO.findById(beerId)

    optionalBeer.map {
      case Some(s) => Ok(Json.toJson(s))
      case None =>
        // Send back a 404 Not Found HTTP status to the client if the Beer does not exist.
        NotFound(Json.obj(
          "status" -> "Not Found",
          "messages" -> ("Beer #" + beerId + " not found.")
        ))
    }
  }

*/
  /**
    * Parse the POST request, validate the request's body, then create a new Beer based on the sent JSON payload, and
    * finally sends back a JSON response.
    * The action expects a request with a Content-Type header of text/json or application/json and a body containing a
    * JSON representation of the entity to create.
    */
  def createBeer = Action.async { implicit request =>
    // `request.body` contains a fully validated `Beer` instance, since it has been validated by the `validateJson`
    // helper above.
    val (name, volume, description, provenance, picture, price, alcool) = form.bindFromRequest.get
    val drink = new Drink(null, name, volume, description, false, picture, 0, 0, price)
    val beer = new Beer(null, 0, provenance, alcool)
    val createdBeer = BeersDAO.insert(drink, beer)

    createdBeer.map(s =>
      Ok(
        Json.obj(
          "status" -> "OK",
          "id" -> s.id,
          "messages" -> ("Beer '" + s.id + " saved.")
        )
      )
    )
  }

  /*
  /**
    * Parse the PUT request, validate the request's body, then update the Beer whose ID matches with the given one,
    * based on the sent JSON payload, and finally sends back a JSON response.
    */
  def updateBeer(beerId: Long) = Action.async(validateJson[Beer]) { request =>
    val newBeer = request.body

    // Try to edit the Beer, then return a 200 OK HTTP status to the client if everything worked.
    BeersDAO.update(beerId, newBeer).map {
      case 1 => Ok(
        Json.obj(
          "status" -> "OK",
          "messages" -> ("Beer '" + newBeer.name + "' updated.")
        )
      )
      case 0 => NotFound(Json.obj(
        "status" -> "Not Found",
        "messages" -> ("Beer #" + beerId + " not found.")
      ))
    }
  }

  /**
    * Try to delete the Beer identified by the given ID, and sends back a JSON response.
    */
  def deleteBeer(beerId: Long) = Action.async {
    BeersDAO.delete(beerId).map {
      case 1 => Ok(
        Json.obj(
          "status"  -> "OK",
          "messages" -> ("Beer #" + beerId + " deleted.")
        )
      )
      case 0 => NotFound(Json.obj(
        "status" -> "Not Found",
        "messages" -> ("Beer #" + beerId + " not found.")
      ))
    }
  }

  def archivedBeer(beerId: Long) = Action.async {
    BeersDAO.archived(beerId).map {
      case 1 => Ok(
        Json.obj(
          "status" -> "OK",
          "messages" -> ("Beer #'" + beerId  + "' updated.")
        )
      )
      case 0 => NotFound(Json.obj(
        "status" -> "Not Found",
        "messages" -> ("Beer #" + beerId + " not found.")
      ))
    }
  }*/

}

case class CreateBeerForm(name: String, volume: Int, description: String, provenance: String, picture: String, price: Int, alcool: BigDecimal)