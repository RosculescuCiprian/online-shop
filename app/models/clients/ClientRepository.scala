package models.clients

import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import tables.{ClientTable, SlickRepository}

import scala.concurrent.{ExecutionContext, Future}

/**
  * A repository for people.
  *
  * @param dbConfigProvider The Play db config provider. Play will inject this for you.
  */
@Singleton
class ClientRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
  extends SlickRepository with ClientTable {
  // We want the JdbcProfile for this provider
  override implicit protected final val dbConfig = dbConfigProvider.get[JdbcProfile]

  // These imports are important, the first one brings db into scope, which will let you do the actual db operations.
  // The second one brings the Slick DSL into scope, which lets you define the table and other queries.
  import dbConfig._
  import dbConfig.profile.api._

  /**
    * Create a person with the given name and age.
    *
    * This is an asynchronous operation, it will return a future of the created person, which can be used to obtain the
    * id for that person.
    */
  def create(name: String, age: Int): Future[Client] = db.run {
    // We create a projection of just the name and age columns, since we're not inserting a value for the id column
    (clients.map(p => (p.name, p.age))
      // Now define it to return the id, because we want to know what id was generated for the person
      returning clients.map(_.id)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into ((nameAge, id) => Client(id, nameAge._1, nameAge._2))
      // And finally, insert the person into the database
      ) += (name, age)
  }

  /**
    * List all the people in the database.
    */
  def list(): Future[Seq[Client]] = db.run {
    clients.result
  }

  def getClientById(id:Long):Future[Option[Client]] = db.run {
    clients.filter(_.id === id).result.headOption
  }
}
