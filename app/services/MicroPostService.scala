package services

import jp.t2v.lab.play2.pager.{ Pager, SearchResult }
import models.MicroPost
import scalikejdbc.{ AutoSession, DBSession }

import scala.util.Try

trait MicroPostService {

  def create(microPost: MicroPost)(implicit dBSession: DBSession = AutoSession): Try[Long]

  def deleteById(id: Long)(implicit dBSession: DBSession = AutoSession): Try[Int]

  def findByUserId(pager: Pager[MicroPost], userId: Long)(
      implicit dBSession: DBSession = AutoSession
  ): Try[SearchResult[MicroPost]]

  def countBy(userId: Long)(implicit dBSession: DBSession = AutoSession): Try[Long]

  def findAllByWithLimitOffset(pager: Pager[MicroPost], userId: Long)(
      implicit dBSession: DBSession = AutoSession
  ): Try[SearchResult[MicroPost]]
}
