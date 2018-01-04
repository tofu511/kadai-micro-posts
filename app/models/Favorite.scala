package models

import java.time.ZonedDateTime

import jp.t2v.lab.play2.pager.{ OrderType, Sortable }
import scalikejdbc._
import jsr310._
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
    merge = (uf, f) => uf.copy(user = f)
  ).includes[User](
    merge = (favorites, users) =>
      favorites.map { fav =>
        users
          .find(user => fav.microPost.exists(_.id == user.id))
          .map(user => fav.copy(user = Some(user)))
          .getOrElse(fav)
    }
  )

  lazy val m = MicroPost.createAlias("m")

  lazy val microPostRef = belongsToWithAliasAndFkAndJoinCondition[MicroPost](
    right = MicroPost -> m,
    fk = "microPostId",
    on = sqls.eq(defaultAlias.microPostId, m.id),
    merge = (uf, f) => uf.copy(microPost = f)
  ).includes[MicroPost](
    merge = (favorites, microposts) =>
      favorites.map { fav =>
        microposts
          .find(mp => fav.microPost.exists(_.id == mp.id))
          .map(mp => fav.copy(microPost = Some(mp)))
          .getOrElse(fav)
    }
  )

  lazy val allAssociations: CRUDFeatureWithId[Long, Favorite] = joins(userRef, microPostRef)

  override def tableName = "favorites"

  override def defaultAlias: Alias[Favorite] = createAlias("f")

  override def extract(rs: WrappedResultSet, n: ResultName[Favorite]): Favorite =
    autoConstruct(rs, n, "user", "microPost")

  private def toNamedValues(record: Favorite): Seq[(Symbol, Any)] = Seq(
    'userId      -> record.userId,
    'microPostId -> record.microPostId,
    'createAt    -> record.createAt,
    'updateAt    -> record.updateAt
  )

  def create(favorite: Favorite)(implicit session: DBSession): Long =
    createWithAttributes(toNamedValues(favorite): _*)

  def update(favorite: Favorite)(implicit session: DBSession): Int =
    updateById(favorite.id.get).withAttributes(toNamedValues(favorite): _*)

  implicit object sortable extends Sortable[Favorite] {
    override val default: (String, OrderType) = ("id", OrderType.Descending)
    override val defaultPageSize: Int         = 10
    override val acceptableKeys: Set[String]  = Set("id")
  }
}
