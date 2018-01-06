package controllers

import javax.inject.{ Inject, Singleton }

import jp.t2v.lab.play2.auth.AuthenticationElement
import jp.t2v.lab.play2.pager.{ Pager, Sortable }
import models.{ Favorite, MicroPost, User }
import play.api.Logger
import play.api.i18n.{ I18nSupport, Messages, MessagesApi }
import play.api.mvc.{ Action, AnyContent, Controller }
import services.{ FavoriteService, MicroPostService, UserFollowService, UserService }

@Singleton
class UsersController @Inject()(val messagesApi: MessagesApi,
                                val userService: UserService,
                                val userFollowService: UserFollowService,
                                val microPostService: MicroPostService,
                                val favoriteService: FavoriteService)
    extends Controller
    with I18nSupport
    with AuthConfigSupport
    with AuthenticationElement {

  def index(pager: Pager[models.User]): Action[AnyContent] = StackAction { implicit request =>
    userService
      .findAll(pager)
      .map { users =>
        Ok(views.html.users.index(loggedIn, users))
      }
      .recover {
        case e: Exception =>
          Logger.error(s"occurred error", e)
          Redirect(routes.UsersController.index(Pager.default))
            .flashing("failure" -> Messages("InternalError"))
      }
      .getOrElse(InternalServerError(Messages("InternalError")))
  }

  def show(userId: Long, page: Int): Action[AnyContent] = StackAction { implicit request =>
    val triedUserOpt        = userService.findById(userId)
    val triedUserFollows    = userFollowService.findById(loggedIn.id.get)
    val pager               = createPager[MicroPost](page)
    val triedMicroPosts     = microPostService.findByUserId(pager, userId)
    val triedFollowingsSize = userFollowService.countByUserId(userId)
    val triedFollowersSize  = userFollowService.countByFollowId(userId)
    val triedFavorites      = favoriteService.findById(loggedIn.id.get)
    val triedFavoriteSize   = favoriteService.countByUserId(loggedIn.id.get)
    (for {
      userOpt        <- triedUserOpt
      userFollows    <- triedUserFollows
      microPosts     <- triedMicroPosts
      followingsSize <- triedFollowingsSize
      followersSize  <- triedFollowersSize
      favorites      <- triedFavorites
      favoriteSize   <- triedFavoriteSize
    } yield {
      userOpt.map { user =>
        Ok(
          views.html.users
            .show(loggedIn, user, userFollows, microPosts, followingsSize, followersSize, favorites, favoriteSize)
        )
      }.get
    }).recover {
        case e: Exception =>
          Logger.error(s"occurred error in UsersController#show", e)
          Redirect(routes.UsersController.index(Pager.default))
            .flashing("failure" -> Messages("InternalError"))
      }
      .getOrElse(InternalServerError(Messages("InternalError")))
  }

  def getFollowers(userId: Long, page: Int): Action[AnyContent] = StackAction { implicit request =>
    val targetUser           = userService.findById(userId).get.get
    val triedMaybeUserFollow = userFollowService.findById(loggedIn.id.get)
    val pager                = createPager[models.User](page)
    val triedFollowers       = userFollowService.findFollowersByUserId(pager, userId)
    val triedMicroPostsSize  = microPostService.countBy(userId)
    val triedFollowingsSize  = userFollowService.countByUserId(userId)
    val triedFavoriteSize    = favoriteService.countByUserId(loggedIn.id.get)
    (for {
      userFollows    <- triedMaybeUserFollow
      followers      <- triedFollowers
      microPostSize  <- triedMicroPostsSize
      followingsSize <- triedFollowingsSize
      favoriteSize   <- triedFavoriteSize
    } yield {
      Ok(
        views.html.users.followers(
          loggedIn,
          targetUser,
          userFollows,
          followers,
          microPostSize,
          followingsSize,
          favoriteSize
        )
      )
    }).recover {
        case e: Exception =>
          Logger.error("occurred error in UsersController#getFollowers", e)
          Redirect(routes.UsersController.index(Pager.default))
            .flashing("failure" -> Messages("InternalError"))
      }
      .getOrElse(InternalServerError(Messages("InternalError")))
  }

  def getFollowings(userId: Long, page: Int): Action[AnyContent] = StackAction { implicit request =>
    val targetUser          = userService.findById(userId).get.get
    val triedUserFollows    = userFollowService.findById(loggedIn.id.get)
    val pager               = createPager[models.User](page)
    val triedFollowings     = userFollowService.findFollowingsByUserId(pager, userId)
    val triedMicroPostsSize = microPostService.countBy(userId)
    val triedFollowersSize  = userFollowService.countByFollowId(userId)
    val triedFavoriteSize   = favoriteService.countByUserId(loggedIn.id.get)
    (for {
      userFollows    <- triedUserFollows
      followings     <- triedFollowings
      microPostsSize <- triedMicroPostsSize
      followersSize  <- triedFollowersSize
      favoriteSize   <- triedFavoriteSize
    } yield {
      Ok(
        views.html.users.followings(
          loggedIn,
          targetUser,
          userFollows,
          followings,
          microPostsSize,
          followersSize,
          favoriteSize
        )
      )
    }).recover {
        case e: Exception =>
          Logger.error("occurred error in UsersController#getFollowings", e)
          Redirect(routes.UsersController.index(Pager.default))
            .flashing("failure" -> Messages("InternalError"))
      }
      .getOrElse(InternalServerError(Messages("InternalError")))

  }

  def getFavorites(userId: Long, page: Int): Action[AnyContent] = StackAction { implicit request =>
    val triedUserOpt        = userService.findById(userId)
    val triedUserFollows    = userFollowService.findById(loggedIn.id.get)
    val pager               = createPager[MicroPost](page)
    val triedMicroPosts     = favoriteService.findFavoritesByUserId(pager, userId)
    val triedFollowingsSize = userFollowService.countByUserId(userId)
    val triedFollowersSize  = userFollowService.countByFollowId(userId)
    val triedFavorites      = favoriteService.findById(loggedIn.id.get)
    val triedFavoriteSize   = favoriteService.countByUserId(loggedIn.id.get)
    (for {
      userOpt        <- triedUserOpt
      userFollows    <- triedUserFollows
      microPosts     <- triedMicroPosts
      followingsSize <- triedFollowingsSize
      followersSize  <- triedFollowersSize
      favorites      <- triedFavorites
      favoriteSize   <- triedFavoriteSize
    } yield {
      userOpt.map { user =>
        Ok(
          views.html.users
            .favorites(loggedIn, user, userFollows, microPosts, followingsSize, followersSize, favorites, favoriteSize)
        )
      }.get
    }).recover {
        case e: Exception =>
          Logger.error("occurred error in UsersController#getFavorites", e)
          Redirect(routes.UsersController.index(Pager.default))
            .flashing("failure" -> Messages("InternalError"))
      }
      .getOrElse(InternalServerError(Messages("InternalError")))
  }

  private def createPager[A](page: Int)(implicit sortable: Sortable[A]): Pager[A] =
    Pager(page, sortable.defaultPageSize, sortable.defaultSorter, sortable.optionalDefaultSorters: _*)

}
