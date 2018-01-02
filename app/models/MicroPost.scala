package models

import java.time.ZonedDateTime

import jp.t2v.lab.play2.pager.{ OrderType, Sortable }
import scalikejdbc._
import jsr310._
import skinny.orm._

/**
  * Micropost
  */
case class MicroPost(id: Option[Long] = None,
                     userId: Long,
                     content: String,
                     createAt: ZonedDateTime = ZonedDateTime.now(),
                     updateAt: ZonedDateTime = ZonedDateTime.now(),
                     user: Option[User] = None)

object MicroPost extends SkinnyCRUDMapper[MicroPost] {

  override def tableName = "micro_posts"

  override def defaultAlias: Alias[MicroPost] = createAlias("m")

  belongsTo[User](User, (uf, u) => uf.copy(user = u)).byDefault

  override def extract(rs: WrappedResultSet, n: ResultName[MicroPost]): MicroPost =
    autoConstruct(rs, n, "user") // userを除外する

  private def toNamedValues(record: MicroPost): Seq[(Symbol, Any)] = Seq(
    'userId   -> record.userId,
    'content  -> record.content,
    'createAt -> record.createAt,
    'updateAt -> record.updateAt
  )

  def create(micropost: MicroPost)(implicit session: DBSession): Long =
    createWithAttributes(toNamedValues(micropost): _*)

  def update(micropost: MicroPost)(implicit session: DBSession): Int =
    updateById(micropost.id.get).withAttributes(toNamedValues(micropost): _*)

  implicit object sortable extends Sortable[MicroPost] {
    override val default: (String, OrderType) = ("id", OrderType.Descending)
    override val defaultPageSize: Int         = 10
    override val acceptableKeys: Set[String]  = Set("id")
  }
}
