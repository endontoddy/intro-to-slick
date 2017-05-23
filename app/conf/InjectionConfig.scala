package conf

import com.google.inject._
import net.codingwell.scalaguice.ScalaModule
import play.api.db.slick.DatabaseConfigProvider
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

class InjectionConfig extends AbstractModule with ScalaModule {
  def configure(): Unit = {}

  @Provides
  def provideDbConf(dbConfigProvider: DatabaseConfigProvider): DatabaseConfig[JdbcProfile] =
    dbConfigProvider.get[JdbcProfile]

}
