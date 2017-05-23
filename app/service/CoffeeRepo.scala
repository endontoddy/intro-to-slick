package service

import javax.inject.Inject

import db.WithTables
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class CoffeeRepo@Inject() (val dbConfig: DatabaseConfig[JdbcProfile])(implicit ec: ExecutionContext)
  extends WithTables {

  import dbConfig.profile.api._
  private val db = dbConfig.db


  // See how our CoffeeRow object is returned if a query returns all columns
  def all: Future[Seq[CoffeeRow]] =
    db.run(coffees.result)

  // Here we filter on the joined supplier table to get a list of Coffees
  //
  // SELECT * FROM coffees c JOIN suppliers s ON s.id = c.supplier_id WHERE s.name = ?;
  //
  def bySupplierName(supplierName: String): Future[Seq[CoffeeRow]] = {
    val query = for {
      (_, cs) <- suppliers.filter(_.name === supplierName) join coffees on (_.id === _.supplierId)
    } yield cs

    db.run(query.result)
  }

  // Here we filter on the joined supplier table to get a list of Coffees
  //
  // SELECT * FROM coffees WHERE supplier_id IN (SELECT id FROM supplier WHERE name = ?);
  //
  def bySupplierName2(supplierName: String): Future[Seq[CoffeeRow]] = {

    val query = for {
      ss <- suppliers.filter(_.name === supplierName).map(_.id)
      cs <- coffees.filter(_.supplierId === ss)
    } yield cs

    db.run(query.result)
  }

  // A query using plain SQL
  //
  // Note that the $name is safe, as the sql macro will escape it correctly
  def priceByName(name: String): Future[Option[Double]] =
  db.run(
    sql"""
         SELECT price FROM coffees WHERE name = $name LIMIT 1);
       """.as[Double].headOption)



}
