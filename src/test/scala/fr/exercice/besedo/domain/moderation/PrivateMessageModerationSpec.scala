package fr.exercice.besedo.domain.moderation

import fr.exercice.besedo.domain.ModelBuilder
import fr.exercice.besedo.domain.error.ModerationRuleError
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PrivateMessageModerationSpec extends AnyWordSpec with Matchers {
  "a private message" should {
    "be rejected" when {
      "there is an email in the body" in {
        val document = ModelBuilder.buildPrivateMessage(body =
          "please contact me at contact-trustme@scampro.com for more information"
        )
        PrivateMessageModeration.checkDocument(document) shouldBe Left(ModerationRuleError.Contact)
      }
      "there is an email in the subject" in {
        val document = ModelBuilder.buildPrivateMessage(subject =
          "please contact me at contact-trustme@scampro.com for more information"
        )
        PrivateMessageModeration.checkDocument(document) shouldBe Left(ModerationRuleError.Contact)
      }
      "there is an url in the body" in {
        val document = ModelBuilder.buildPrivateMessage(body =
          "please check our website http://trustme.scampro.com for more information"
        )
        PrivateMessageModeration.checkDocument(document) shouldBe Left(ModerationRuleError.Contact)
      }
      "there is an url in the subject" in {
        val document = ModelBuilder.buildPrivateMessage(subject =
          "please check our website http://trustme.scampro.com for more information"
        )
        PrivateMessageModeration.checkDocument(document) shouldBe Left(ModerationRuleError.Contact)
      }
      "the body makes no sense" in {
        PrivateMessageModeration.checkDocument(ModelBuilder.buildPrivateMessage(body = "bdy")) shouldBe Left(
          ModerationRuleError.Nonsense
        )
      }
      "the subject makes no sense" in {
        PrivateMessageModeration.checkDocument(ModelBuilder.buildPrivateMessage(subject = "bdy")) shouldBe Left(
          ModerationRuleError.Nonsense
        )
      }
      "when nothing is valid" in {
        val document = ModelBuilder.buildPrivateMessage(
          body =
            "please check our website http://trustme.scampro.com or contact me at contact-trustme@scampro.com for more information",
          subject =
            "please check our website http://trustme.scampro.com or contact me at contact-trustme@scampro.com for more information"
        )
        PrivateMessageModeration.checkDocument(document) shouldBe Left(ModerationRuleError.Contact)
      }
    }
    "be valid" when {
      "all rules are respected" in {
        val document = ModelBuilder.buildPrivateMessage()
        PrivateMessageModeration.checkDocument(document) shouldBe Right(document)
      }
    }
  }
}
