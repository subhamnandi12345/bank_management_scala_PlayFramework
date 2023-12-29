// src/main/scala/models/BankManagementSystemImpl.scala
package models

import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet}

class MySQLBankManagementSystem extends BankManagementSystem {
  val url = "jdbc:mysql://localhost:3306/bankdb"
  val username = "root"
  val password = "root@123"
  // Create a connection
  val connection: Connection = DriverManager.getConnection(url, username, password)

  // Implement BankManagementSystem methods using MySQL queries

  override def addCustomer(name: String, id: Int, balance: Double): Unit = {
    val query = "INSERT INTO customers (id, name, balance) VALUES (?, ?, ?)"
    val preparedStatement: PreparedStatement = connection.prepareStatement(query)
    preparedStatement.setInt(1, id)
    preparedStatement.setString(2, name)
    preparedStatement.setDouble(3, balance)
    preparedStatement.executeUpdate()
    preparedStatement.close()
  }

  override def deleteCustomer(id: Int): Unit = {
    val query = "DELETE FROM customers WHERE id = ?"
    val preparedStatement: PreparedStatement = connection.prepareStatement(query)
    preparedStatement.setInt(1, id)
    preparedStatement.executeUpdate()
    preparedStatement.close()
  }

  override def showCustomer(id: Int): Option[Customer] = {
    val query = "SELECT * FROM customers WHERE id = ?"
    val preparedStatement: PreparedStatement = connection.prepareStatement(query)
    preparedStatement.setInt(1, id)
    val resultSet: ResultSet = preparedStatement.executeQuery()

    val customerOption: Option[Customer] =
      if (resultSet.next()) {
        Some(Customer(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getDouble("balance")))
      } else {
        None
      }

    preparedStatement.close()
    resultSet.close()

    customerOption
  }

  def transferMoney(fromId: Int, toId: Int, amount: Double): Unit = {
    val withdrawQuery = "UPDATE customers SET balance = balance - ? WHERE id = ?"
    val depositQuery = "UPDATE customers SET balance = balance + ? WHERE id = ?"

    val withdrawStatement: PreparedStatement = connection.prepareStatement(withdrawQuery)
    withdrawStatement.setDouble(1, amount)
    withdrawStatement.setInt(2, fromId)
    withdrawStatement.executeUpdate()
    withdrawStatement.close()

    val depositStatement: PreparedStatement = connection.prepareStatement(depositQuery)
    depositStatement.setDouble(1, amount)
    depositStatement.setInt(2, toId)
    depositStatement.executeUpdate()
    depositStatement.close()
  }

}
