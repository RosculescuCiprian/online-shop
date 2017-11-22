package tables

import models.Client

trait ClientTable {

  this:SlickRepository =>

  import driver.api._

  /**
    * Here we define the table. It will have a name of people
    */
  protected class Clients(tag: Tag) extends Table[Client](tag, "clients") {

    /** The ID column, which is the primary key, and auto incremented */
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    /** The name column */
    def name = column[String]("name")

    /** The age column */
    def age = column[Int]("age")

    /**
      * This is the tables default "projection".
      *
      * It defines how the columns are converted to and from the Person object.
      *
      * In this case, we are simply passing the id, name and page parameters to the Person case classes
      * apply and unapply methods.
      */
    def * = (id, name, age) <> ((Client.apply _).tupled, Client.unapply)
  }

  protected def clients = TableQuery[Clients]
}
