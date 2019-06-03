package controllers

import dao.{BeersDAO, SoftDrinksDAO}
import javax.inject.{Inject, Singleton}
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class DrinksController @Inject()(cc: ControllerComponents, BeersDAO: BeersDAO, SoftDrinksDAO: SoftDrinksDAO) extends AbstractController(cc) {

  // Convert a Drink-model object into a JsValue representation, which means that we serialize it into JSON.
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
    )(Beer.apply _)*/

  /** Title */
  val title = "Drinks Chillout"


  /**
    * This helper parses and validates JSON using the implicit `jsonToDrink` above, returning errors if the parsed
    * json fails validation.
    */
  def validateJson[A : Reads] = parse.json.validate(
    _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )

  /**
    * Get the list of all existing Drinks, then return it.
    * The Action.async is used because the request is asynchronous.
    */
  def getDrinks = Action.async {
    val beersList = BeersDAO.list()
    val softDrinksList = SoftDrinksDAO.list()

    for {
      beers <- beersList
      softDrinks <- softDrinksList
    } yield Ok(views.html.drinks(title, beers, softDrinks))
  }


  /**
    * Parse the POST request, validate the request's body, then create a new Drink based on the sent JSON payload, and
    * finally sends back a JSON response.
    * The action expects a request with a Content-Type header of text/json or application/json and a body containing a
    * JSON representation of the entity to create.
    */


}
