package service

import helpers.WithDb
import org.specs2.mutable.Specification

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class CoffeeRepoTest extends Specification {

  "all" should {

    "Read coffee accurately" in new WithDb {

      val coffeeRepo = new CoffeeRepo(dbConf)

      val coffees: Seq[String] = Await.result(coffeeRepo.bySupplierName3("BEANIES"), Duration.Inf).map(_.name)

      coffees mustEqual Seq("B1")
    }
  }
}
