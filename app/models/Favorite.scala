package models

import java.time.ZonedDateTime

import scalikejdbc._, jsr310._
import skinny.orm._
import skinny.orm.feature._

/**
  * Favorite
  */
case class Favorite(id: Option[Long],
                    userId: Long,
                    microPostId: Long,
                    createAt: ZonedDateTime = ZonedDateTime.now(),
                    updateAt: ZonedDateTime = ZonedDateTime.now(),
                    user: Option[User] = None,
                    microPost: Option[MicroPost] = None)

object Favorite extends SkinnyCRUDMapper[Favorite] {

  lazy val u = User.createAlias("u")

  lazy val userRef = belongsToWithAliasAndFkAndJoinCondition[User](
    right = User -> u,
    fk = "userId",
    on = sqls.eq(defaultAlias.userId, u.id),
    merge = (f, u) => f.copy(user = u)
  )

  lazy val m = MicroPost.createAlias("m")

  lazy val microPostRef = belongsToWithAliasAndFkAndJoinCondition[MicroPost](
    right = MicroPost -> m,
    fk = "microPostId",
    on = sqls.eq(defaultAlias.microPostId, m.id),
    merge = (f, m) => f.copy(microPost = m)
  )

  lazy val allAssociations: CRUDFeatureWithId[Long, Favorite] = joins(userRef, microPostRef)

  override def tableName = "favorites"

  override def defaultAlias: Alias[Favorite] = createAlias("f")

  override def extract(rs: WrappedResultSet, n: ResultName[Favorite]): Favorite =
    autoConstruct(rs, n, "user", "microPost")

  private def toNamedValues(record: Favorite): Seq[(Symbol, Any)] = Seq(
    'userId      -> record.userId,
    'micropostId -> record.microPostId,
    'createAt    -> record.createAt,
    'updateAt    -> record.updateAt
  )

  def create(favorite: Favorite)(implicit session: DBSession): Long =
    createWithAttributes(toNamedValues(favorite): _*)

  def update(favorite: Favorite)(implicit session: DBSession): Int =
    updateById(favorite.id.get).withAttributes(toNamedValues(favorite): _*)

}
