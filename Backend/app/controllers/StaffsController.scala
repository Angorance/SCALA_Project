package controllers


import dao.StaffsDAO
import javax.inject.{Inject, Singleton}
import models.Staff
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class StaffsController @Inject()(cc: ControllerComponents, StaffsDAO: StaffsDAO) extends AbstractController(cc) {

  // Convert a Staff-model object into a JsValue representation, which means that we serialize it into JSON.
  implicit val StaffToJson: Writes[Staff] = (
    (JsPath \ "id").write[Option[Long]] and
      (JsPath \ "pseudo").write[String] and
      (JsPath \ "password").write[String]
    // Use the default 'unapply' method (which acts like a reverted constructor) of the Staff case class if order to get
    // back the Staff object's arguments and pass them to the JsValue.
    )(unlift(Staff.unapply))

  // Convert a JsValue representation into a Staff-model object, which means that we deserialize the JSON.
  implicit val jsonToStaff: Reads[Staff] = (
    // In order to be valid, the Staff must have first and last names that are 2 characters long at least, as well as
    // an age that is greater than 0.
    (JsPath \ "id").readNullable[Long] and
      (JsPath \ "pseudo").read[String](minLength[String](2)) and
      (JsPath \ "password").read[String](minLength[String](2))
    // Use the default 'apply' method (which acts like a constructor) of the Staff case class with the JsValue in order
    // to construct a Staff object from it.
    )(Staff.apply _)

  /**
    * This helper parses and validates JSON using the implicit `jsonToStaff` above, returning errors if the parsed
    * json fails validation.
    */
  def validateJson[A : Reads] = parse.json.validate(
    _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )

  /**
    * Get the list of all existing Staffs, then return it.
    * The Action.async is used because the request is asynchronous.
    */
  def getStaffs = Action.async {
    val StaffsList = StaffsDAO.list()
    StaffsList map (s => Ok(Json.toJson(s)))
  }

  /**
    * Parse the POST request, validate the request's body, then create a new Staff based on the sent JSON payload, and
    * finally sends back a JSON response.
    * The action expects a request with a Content-Type header of text/json or application/json and a body containing a
    * JSON representation of the entity to create.
    */
  def createStaff = Action.async(validateJson[Staff]) { implicit request =>
    // `request.body` contains a fully validated `Staff` instance, since it has been validated by the `validateJson`
    // helper above.
    val Staff = request.body
    val createdStaff = StaffsDAO.insert(Staff)

    createdStaff.map(s =>
      Ok(
        Json.obj(
          "status" -> "OK",
          "id" -> s.id,
          "message" -> ("Staff '" + s.pseudo + " saved.")
        )
      )
    )
  }

  /**
    * Get the Staff identified by the given ID, then return it as JSON.
    */
  def getStaff(StaffId: Long) = Action.async {
    val optionalStaff = StaffsDAO.findById(StaffId)

    optionalStaff.map {
      case Some(s) => Ok(Json.toJson(s))
      case None =>
        // Send back a 404 Not Found HTTP status to the client if the Staff does not exist.
        NotFound(Json.obj(
          "status" -> "Not Found",
          "message" -> ("Staff #" + StaffId + " not found.")
        ))
    }
  }

  /**
    * Parse the PUT request, validate the request's body, then update the Staff whose ID matches with the given one,
    * based on the sent JSON payload, and finally sends back a JSON response.
    */
  def updateStaff(StaffId: Long) = Action.async(validateJson[Staff]) { request =>
    val newStaff = request.body

    // Try to edit the Staff, then return a 200 OK HTTP status to the client if everything worked.
    StaffsDAO.update(StaffId, newStaff).map {
      case 1 => Ok(
        Json.obj(
          "status" -> "OK",
          "message" -> ("Staff '" + newStaff.pseudo + "' updated.")
        )
      )
      case 0 => NotFound(Json.obj(
        "status" -> "Not Found",
        "message" -> ("Staff #" + StaffId + " not found.")
      ))
    }
  }

  /**
    * Try to delete the Staff identified by the given ID, and sends back a JSON response.
    */
  def deleteStaff(StaffId: Long) = Action.async {
    StaffsDAO.delete(StaffId).map {
      case 1 => Ok(
        Json.obj(
          "status"  -> "OK",
          "message" -> ("Staff #" + StaffId + " deleted.")
        )
      )
      case 0 => NotFound(Json.obj(
        "status" -> "Not Found",
        "message" -> ("Staff #" + StaffId + " not found.")
      ))
    }
  }

  def login(StaffPseudo: String, StaffPwd: String): Unit = Action.async {
    val optionalStaff = StaffsDAO.findByPseudo(StaffPseudo)
    optionalStaff.map {
      case Some(s) => Ok(Json.toJson(s))
      case None =>
        // Send back a 404 Not Found HTTP status to the client if the Staff does not exist.
        NotFound(Json.obj(
          "status" -> "Not Found",
          "message" -> ("Staff #" + StaffPseudo + " do not exist")
        ))
    }
  }

}
