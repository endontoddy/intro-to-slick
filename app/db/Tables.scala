package db

import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

trait WithTables {

  // We can keep all of our Table defs in one place for convenience

  // There is also a schema-generator available that can generate this code from an existing database

  val dbConfig: DatabaseConfig[JdbcProfile]
  import dbConfig.profile.api._

  // Coffee table

  case class CoffeeRow(id: Int, name: String, price: Double, onSale: Boolean, supplierId: Int)

  class Coffee(tag: Tag) extends Table[CoffeeRow](tag, "coffee") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name", O.Unique)
    def price = column[Double]("price")
    def onSale = column[Boolean]("on_sale")
    def supplierId = column[Int]("supplier_id")

    def * = (id, name, price, onSale, supplierId) <> (CoffeeRow.tupled, CoffeeRow.unapply)

  }

  val coffees = TableQuery[Coffee]


  // Supplier table

  case class SupplierRow(id: Int, name: String)

  class Suppliers(tag: Tag) extends Table[SupplierRow](tag, "supplier") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name", O.Unique)

    def * = (id, name) <> (SupplierRow.tupled, SupplierRow.unapply)

  }

  val suppliers = TableQuery[Suppliers]




}
