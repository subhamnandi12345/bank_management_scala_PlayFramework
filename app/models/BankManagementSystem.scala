package models

trait BankManagementSystem {
  def addCustomer(name: String, id: Int, balance: Double): Unit
  def deleteCustomer(id: Int): Unit
  def showCustomer(id: Int): Option[Customer]
  def transferMoney(fromId: Int, toId: Int, amount: Double): Unit
  type CustomerOperation = Customer => Unit
  def updateCustomer(id: Int, operation: CustomerOperation): Unit}

case class Customer(id: Int, name: String, balance: Double)

