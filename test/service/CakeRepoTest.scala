package service

import helpers.WithDb
import scala.concurrent.ExecutionContext.Implicits.global
import org.specs2.mutable.Specification

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class CakeRepoTest extends Specification {

  "all" should {

    "Add and read cakes accurately" in new WithDb {

      val cakeRepo = new CakeRepo(dbConf)

      val id: Int = Await.result(cakeRepo.addCake("Biccy", 0.20, false), Duration.Inf)

      Await.result(cakeRepo.all, Duration.Inf) must contain((id, "Biccy", 0.20, false))
    }
  }
}
