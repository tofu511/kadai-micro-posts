package services

import models.User
import scalikejdbc.DBSession

import scala.util.{ Success, Try }

class MockUserService extends UserService {

  override def create(user: User)(implicit dbSession: DBSession): Try[Long] = Success(1)

  override def findByEmail(email: String)(implicit dbSession: DBSession): Try[Option[User]] =
    Success(Some(User(Some(1L), "test", email, "xxx")))
}
