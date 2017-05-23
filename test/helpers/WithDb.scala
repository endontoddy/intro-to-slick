package helpers

import org.specs2.specification.Scope
import slick.jdbc.JdbcProfile
import play.api.db.slick.DatabaseConfigProvider
import play.api.inject.guice.GuiceApplicationBuilder

abstract class WithDb(dbName: String = "default") extends Scope {
  private val app = new GuiceApplicationBuilder().build()
  val dbConf = DatabaseConfigProvider.get[JdbcProfile](dbName)(app)
  val db = dbConf.db
}
