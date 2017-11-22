package tables

import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

trait SlickRepository {

  implicit protected def dbConfig: DatabaseConfig[JdbcProfile]

  /**
    * This can get nasty.
    *
    * It has to be lazy or you end up with runtime NullPointerException because of trait initialization order
    * and it has to be val because of import driver.api._
    *
    * It's final to prevent anyone from overriding it downstream.
    */
  protected lazy final val driver = dbConfig.profile

}
