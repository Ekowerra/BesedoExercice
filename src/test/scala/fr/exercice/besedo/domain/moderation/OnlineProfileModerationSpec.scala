package fr.exercice.besedo.domain.moderation

import fr.exercice.besedo.domain.ModelBuilder
import fr.exercice.besedo.domain.error.ModerationRuleError
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class OnlineProfileModerationSpec extends AnyWordSpec with Matchers {
  "an online profile" should {
    "be rejected" when {
      "the user age is below 18" in {
        val profile = ModelBuilder.buildOnlineProfile(age = 10)
        OnlineProfileModeration.checkDocument(profile) shouldBe Left(ModerationRuleError.Underage)
      }
      "there is an email in the body" in {
        val document = ModelBuilder.buildOnlineProfile(body =
          "please contact me at contact-trustme@scampro.com for more information"
        )
        OnlineProfileModeration.checkDocument(document) shouldBe Left(ModerationRuleError.Contact)
      }
      "there is an email in the subject" in {
        val document = ModelBuilder.buildOnlineProfile(subject =
          "please contact me at contact-trustme@scampro.com for more information"
        )
        OnlineProfileModeration.checkDocument(document) shouldBe Left(ModerationRuleError.Contact)
      }
      "there is an url in the body" in {
        val document = ModelBuilder.buildOnlineProfile(body =
          "please check our website http://trustme.scampro.com for more information"
        )
        OnlineProfileModeration.checkDocument(document) shouldBe Left(ModerationRuleError.Contact)
      }
      "there is an url in the subject" in {
        val document = ModelBuilder.buildOnlineProfile(subject =
          "please check our website http://trustme.scampro.com for more information"
        )
        OnlineProfileModeration.checkDocument(document) shouldBe Left(ModerationRuleError.Contact)
      }
      "the body makes no sense" in {
        OnlineProfileModeration.checkDocument(ModelBuilder.buildOnlineProfile(body = "bdy")) shouldBe Left(
          ModerationRuleError.Nonsense
        )
      }
      "the subject makes no sense" in {
        OnlineProfileModeration.checkDocument(ModelBuilder.buildOnlineProfile(subject = "bdy")) shouldBe Left(
          ModerationRuleError.Nonsense
        )
      }
      "when nothing is valid" in {
        val document = ModelBuilder.buildOnlineProfile(
          body =
            "please check our website http://trustme.scampro.com or contact me at contact-trustme@scampro.com for more information",
          age = 10,
          subject =
            "please check our website http://trustme.scampro.com or contact me at contact-trustme@scampro.com for more information"
        )
        OnlineProfileModeration.checkDocument(document) shouldBe Left(ModerationRuleError.Underage)
      }
    }
    "be valid" when {
      "all rules are respected" in {
        val document = ModelBuilder.buildOnlineProfile()
        OnlineProfileModeration.checkDocument(document) shouldBe Right(document)
      }
    }
  }
}
