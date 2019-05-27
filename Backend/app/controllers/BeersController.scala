package controllers

import dao.BeersDAO
import javax.inject.{Inject, Singleton}
import models.Beer
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class BeersController @Inject()(cc: ControllerComponents, BeersDAO: BeersDAO) extends AbstractController(cc) {

  // Convert a Beer-model object into a JsValue representation, which means that we serialize it into JSON.
  implicit val BeerToJson: Writes[Beer] = (
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

  /**
    * This helper parses and validates JSON using the implicit `jsonToBeer` above, returning errors if the parsed
    * json fails validation.
    */
  def validateJson[A : Reads] = parse.json.validate(
    _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )

  /**
    * Get the list of all existing Beers, then return it.
    * The Action.async is used because the request is asynchronous.
    */
  def getBeers = Action.async {
    val BeersList = BeersDAO.list()
    BeersList map (s => Ok(Json.toJson(s)))
  }

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
          "message" -> ("Beer #" + beerId + " not found.")
        ))
    }
  }

  /**
    * Parse the POST request, validate the request's body, then create a new Beer based on the sent JSON payload, and
    * finally sends back a JSON response.
    * The action expects a request with a Content-Type header of text/json or application/json and a body containing a
    * JSON representation of the entity to create.
    */
  def createBeer = Action.async(validateJson[Beer]) { implicit request =>
    // `request.body` contains a fully validated `Beer` instance, since it has been validated by the `validateJson`
    // helper above.
    val Beer = request.body
    val createdBeer = BeersDAO.insert(Beer)

    createdBeer.map(s =>
      Ok(
        Json.obj(
          "status" -> "OK",
          "id" -> s.id,
          "message" -> ("Beer '" + s.name + " saved.")
        )
      )
    )
  }

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
          "message" -> ("Beer '" + newBeer.name + "' updated.")
        )
      )
      case 0 => NotFound(Json.obj(
        "status" -> "Not Found",
        "message" -> ("Beer #" + beerId + " not found.")
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
          "message" -> ("Beer #" + beerId + " deleted.")
        )
      )
      case 0 => NotFound(Json.obj(
        "status" -> "Not Found",
        "message" -> ("Beer #" + beerId + " not found.")
      ))
    }
  }

  def archivedBeer(beerId: Long) = Action.async {
    BeersDAO.archived(beerId).map {
      case 1 => Ok(
        Json.obj(
          "status" -> "OK",
          "message" -> ("Beer #'" + beerId  + "' updated.")
        )
      )
      case 0 => NotFound(Json.obj(
        "status" -> "Not Found",
        "message" -> ("Beer #" + beerId + " not found.")
      ))
    }
  }

}
