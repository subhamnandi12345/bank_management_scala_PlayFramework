// src/main/scala/controllers/BankController.scala
package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import models._
import models.{Customer, MySQLBankManagementSystem}
import models.BankManagementSystem
import org.apache.pekko.http.scaladsl.model.headers.HttpEncodingRange.*
import play.api.i18n.Lang.jsonTagWrites
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json.JsObject.writes
import play.api.libs.json.OFormat.oFormatFromReadsAndOWrites


@Singleton
class BankController @Inject()(cc: ControllerComponents,bnk : MySQLBankManagementSystem) extends AbstractController(cc) {

  implicit val customerFormat: Format[Customer] = Json.format[Customer]
  implicit val customerWrites: Writes[Customer] = Json.writes[Customer]
  implicit val customerReads: Reads[Customer] = Json.reads[Customer]

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())

  }

  def addCustomerOne = Action(parse.json) { request =>
    val customerData = request.body.as[Customer]
    bnk.addCustomer(customerData.name, customerData.id, customerData.balance)
    Ok(Json.toJson(customerData))
  }

  def deleteCustomerOne(id: Int) = Action {
    bnk.deleteCustomer(id)
    Ok(s"Customer with ID $id deleted.")
  }

  def showCustomerOne(id: Int) = Action {
    val res = bnk.showCustomer(id) map { idd =>
      Json.toJson(idd)
    }
    Ok(res.get)
  }

//  def showCustomer(id: Int) = Action {
//    bnk.showCustomer(id) map {
//      case Some(customer) => Ok(Json.toJson(customer))
//      case None => NotFound(s"Customer with ID $id not found.")
//    }
//  }
  def transferMoney = Action(parse.json) { request =>
    val transferData = request.body.as[JsObject]
    val fromId = (transferData \ "fromId").as[Int]
    val toId = (transferData \ "toId").as[Int]
    val amount = (transferData \ "amount").as[Double]
    bnk.transferMoney(fromId, toId, amount)
    Ok(s"$amount transferred from customer $fromId to customer $toId.")
  }
//  def updateID(id: Int) = Action { implicit request: Request[AnyContent] =>
//
//    def updatedID(id:Int,squareID:Int=>(3*6)): Unit = {
//      println(squareID(id))
//    }
//    val a=updatedID(1,3*8)
//    Ok(s"Updating ID: $a")
//  }

  def processID(id: Int, processFunction: Int => Result): Result = {
    processFunction(id)
  }
  def updateID(id: Int) = Action { implicit request: Request[AnyContent] =>

    def processFunctionForUpdate(id: Int): Result = {
      val a=id*id
      Ok(s"Updating ID: $a")
    }
    // Calling  the higher-order function with the ID and the processing function
    processID(2, processFunctionForUpdate)
  }
}