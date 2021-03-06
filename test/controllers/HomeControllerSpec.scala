package controllers

import jp.t2v.lab.play2.auth.test.Helpers._
import jp.t2v.lab.play2.pager.Pager
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.UserService

class HomeControllerSpec extends PlayFunSpec with GuiceOneAppPerSuite {

  object Config extends AuthConfigSupport {
    override val userService: UserService = app.injector.instanceOf[UserService]
  }

  describe("HomeController") {
    describe("route of HomeController#index") {
      it("should be valid") {
        val result =
          route(app, addCsrfToken(FakeRequest(GET, routes.HomeController.index(Pager.default).toString))).get
        status(result) mustBe OK
      }

      it("should be valid when logged in") {
        val email = "test@test.com"
        val result =
          route(app,
                addCsrfToken(FakeRequest(GET, routes.HomeController.index(Pager.default).toString))
                  .withLoggedIn(Config)(email)).get
        status(result) mustBe OK
        contentAsString(result) must include(email)
      }
    }
  }
}
