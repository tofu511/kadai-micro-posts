package services

import jp.t2v.lab.play2.pager.{ Pager, SearchResult }
import models.{ User, UserFollow }
import scalikejdbc.{ AutoSession, DBSession }

import scala.util.Try

trait UserFollowService {

  def create(userFollow: UserFollow)(implicit dBSession: DBSession = AutoSession): Try[Long]

  def findById(userId: Long)(implicit dBSession: DBSession = AutoSession): Try[List[UserFollow]]

  def findByFollowId(followId: Long)(implicit dBSession: DBSession = AutoSession): Try[Option[UserFollow]]

  def findFollowersByUserId(pager: Pager[User], userId: Long)(
      implicit dBSession: DBSession = AutoSession
  ): Try[SearchResult[User]]

  def findFollowingsByUserId(pager: Pager[User], userId: Long)(
      implicit dBSession: DBSession = AutoSession
  ): Try[SearchResult[User]]

  def countByUserId(userId: Long)(implicit dBSession: DBSession = AutoSession): Try[Long]

  def countByFollowId(followId: Long)(implicit dBSession: DBSession = AutoSession): Try[Long]

  def deleteBy(userId: Long, followId: Long)(implicit dBSession: DBSession = AutoSession): Try[Int]

}
