package services
import java.time.ZonedDateTime

import jp.t2v.lab.play2.pager.{ Pager, SearchResult }
import models.MicroPost
import scalikejdbc.DBSession

import scala.util.{ Success, Try }

class MockMicroPostService extends MicroPostService {

  private val now = ZonedDateTime.now()

  override def create(microPost: MicroPost)(implicit dBSession: DBSession): Try[Long] = Success(1L)

  override def deleteById(id: Long)(implicit dBSession: DBSession): Try[Int] = Success(1)

  override def findByUserId(pager: Pager[MicroPost],
                            userId: Long)(implicit dBSession: DBSession): Try[SearchResult[MicroPost]] =
    Success(SearchResult(pager, 1)(_ => List(MicroPost(Some(1), 1L, "test", now, now))))

  override def countBy(userId: Long)(implicit dBSession: DBSession): Try[Long] = Success(1L)

  override def findAllByWithLimitOffset(pager: Pager[MicroPost],
                                        userId: Long)(implicit dBSession: DBSession): Try[SearchResult[MicroPost]] =
    Success(SearchResult(pager, 1)(_ => List(MicroPost(Some(1), 1L, "test", now, now))))
}
