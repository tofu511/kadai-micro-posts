package services

import javax.inject.Singleton

import jp.t2v.lab.play2.pager.scalikejdbc._
import jp.t2v.lab.play2.pager.{ Pager, SearchResult }
import models.MicroPost
import scalikejdbc._

import scala.util.Try

@Singleton
class MicroPostServiceImpl extends MicroPostService {

  override def create(microPost: MicroPost)(implicit dBSession: DBSession): Try[Long] = Try {
    MicroPost.create(microPost)
  }

  override def deleteById(id: Long)(implicit dBSession: DBSession): Try[Int] = Try {
    MicroPost.deleteById(id)
  }

  override def findByUserId(pager: Pager[MicroPost],
                            userId: Long)(implicit dBSession: DBSession): Try[SearchResult[MicroPost]] =
    countBy(userId).map { size =>
      SearchResult(pager, size)(findAllByWithLimitOffset(userId))
    }

  override def countBy(userId: Long)(implicit dBSession: DBSession): Try[Long] = Try {
    MicroPost.countBy(sqls.eq(MicroPost.defaultAlias.userId, userId))
  }

  override def findAllByWithLimitOffset(pager: Pager[MicroPost], userId: Long)(
      implicit dBSession: DBSession
  ): Try[SearchResult[MicroPost]] = Try {
    val size = MicroPost.countBy(sqls.eq(MicroPost.defaultAlias.userId, userId))
    SearchResult(pager, size)(findAllByWithLimitOffset(userId))
  }

  private def findAllByWithLimitOffset(
      userId: Long
  )(pager: Pager[MicroPost])(implicit dBSession: DBSession): Seq[MicroPost] =
    MicroPost.findAllByWithLimitOffset(
      sqls.eq(MicroPost.defaultAlias.userId, userId),
      pager.limit,
      pager.offset,
      pager.allSorters.map(_.toSQLSyntax(MicroPost.defaultAlias))
    )

}
