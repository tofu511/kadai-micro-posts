package services

import jp.t2v.lab.play2.pager.{ Pager, SearchResult }
import models.{ Favorite, MicroPost }
import scalikejdbc.{ AutoSession, DBSession }

import scala.util.Try

trait FavoriteService {

  def create(favorite: Favorite)(implicit dBSession: DBSession = AutoSession): Try[Long]

  def findById(userId: Long)(implicit dBSession: DBSession = AutoSession): Try[List[Favorite]]

  def findFavoritesByUserId(pager: Pager[MicroPost], userId: Long)(
      implicit dBSession: DBSession = AutoSession
  ): Try[SearchResult[MicroPost]]

  def countByUserId(userId: Long)(implicit dBSession: DBSession = AutoSession): Try[Long]

  def deleteBy(userId: Long, microPostId: Long)(implicit dBSession: DBSession = AutoSession): Try[Int]

}
