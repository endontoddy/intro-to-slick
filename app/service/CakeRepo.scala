package service

import javax.inject.Inject

import slick.jdbc.JdbcProfile
import slick.basic.DatabaseConfig

import scala.concurrent.{ExecutionContext, Future}

class CakeRepo@Inject() (val dbConfig: DatabaseConfig[JdbcProfile])(implicit ec: ExecutionContext) {
  // Play provides the configured database object via Injection

  // This imports the correct version of the Slick DSL, based on
  // the database we're using.
  // The DSL is used to create the table definitions and write queries
  import dbConfig.profile.api._

  // The 'db' object is used to execute queries (asynchronously)
  private val db = dbConfig.db

  // For each db table, we need a class that describes it
  // The second parameter to Table ("cake" here), should match the name of the table
  // in the database
  class Cakes(tag: Tag) extends Table[(Int, String, Double, Boolean)](tag, "cake") {

    // Each column in the table is described here.
    // The parameters are: The type, column-name in the db, and any other meta flags (like 'Unique')
    // Columns are assumed to be 'Not Null' unless the type is defined as an Option
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name", O.Unique)
    def price = column[Double]("price")
    def onSale = column[Boolean]("on_sale")

    // A description of all rows (the 'projection')
    def * = (id, name, price, onSale)

  }

  // The table's 'query' object, that can be used to interact with it
  val cakes = TableQuery[Cakes]


  // This returns all rows from the table, each row represented by a tuple
  //  of the id, name, price and sale-status
  //
  //  SELECT * FROM cake;
  //
  def all: Future[Seq[(Int, String, Double, Boolean)]] = {
    val query = cakes.result
    db.run(query)
  }


  // This returns all rows from the table that have a given sale-status
  // Note the necessity of a triple equals operator '===' to indicate two values should be equal
  // (the usual Scala == will not work when defining Slick queries!)
  //
  //  SELECT * FROM cake WHERE saleStatus = ?;
  //
  def filterByOnSale(saleStatus: Boolean): Future[Seq[(Int, String, Double, Boolean)]] =
    db.run(
      cakes.filter(_.onSale === saleStatus).result
    )


  // This will return an Option of a cake's price, if it finds a cake
  // with the given name
  // Note that we have factored out part of the query so it can be used
  // in 'updatePrice' too
  //
  //  SELECT price FROM cake WHERE name = ?;
  //
  def getPriceByName(name: String): Future[Option[Double]] =
    db.run(
      priceByNameQuery(name)
        .result
        .headOption
    )


  // Note here how we have used map to filter the list of columns we wish to return from
  // the table
  private def priceByNameQuery(name: String) =
    cakes.filter(_.name === name)
      .map(_.price)


  // This will update the price of a cake based on the name. Note how it works by filtering
  // to the fields / rows we wish to change, and then applying the new value
  // The return value is the number of rows updated
  //
  //  UPDATE cake SET price = ? WHERE name = ?;
  //
  def updatePrice(name: String, newPrice: Double): Future[Int] =
    db.run(
      priceByNameQuery(name)
        .update(newPrice)
    )


  // This will insert a new row, and return the new auto-incremented id
  // Note the 'returning' function used to define what the return value should be
  // '+=' will insert one row, '++=' will insert a List of rows. Note how the operators
  // are the same as those you find in the standard Scala collection libraries
  //
  //  INSERT INTO cake (name, price, onSale) VALUES (?, ?, ?);
  //
  def addCake(name: String, price: Double, onSale: Boolean): Future[Int] =
    db.run(
      cakes returning cakes.map(_.id) += (0, name, price, onSale)
    )


  // This will delete a cake based on the name
  // The return value here is the number of rows deleted
  //
  // DELETE FROM cake WHERE name = ?;
  //
  def removeCake(name: String): Future[Int] =
    db.run(
      cakes.filter(_.name === name)
        .delete
    )

}
