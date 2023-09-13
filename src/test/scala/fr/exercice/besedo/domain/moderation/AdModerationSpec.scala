package fr.exercice.besedo.domain.moderation

import fr.exercice.besedo.domain.ModelBuilder
import fr.exercice.besedo.domain.error.ModerationRuleError
import fr.exercice.besedo.model.document.AdCategory
import fr.exercice.besedo.model.document.AdCategory.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class AdModerationSpec extends AnyWordSpec with Matchers with ScalaCheckPropertyChecks {
  "an ad" should {
    "be rejected" when {
      "it's price is outside boundaries" in {
        val boundaries = Table(
          ("ad-category", "value"),
          (entertainment, 0.1f),
          (pets, 0.1f),
          (computers, 0.1f),
          (food, 0.1f),
          (miscellaneous, 0.1f),
          (entertainment, 10000f),
          (pets, 10000f),
          (computers, 10000f),
          (food, 10000f),
          (miscellaneous, 10000f),
          (entertainment, 1f),
          (pets, 500f),
          (computers, 100f),
          (food, 10f),
          (miscellaneous, 1f),
          (entertainment, 1000f),
          (pets, 1000f),
          (computers, 3500f),
          (food, 200f),
          (miscellaneous, 100f)
        )
        forAll(boundaries) { (category, price) =>
          val document = ModelBuilder.buildAd(category = category, price = price)
          AdModeration.checkDocument(document) shouldBe Left(ModerationRuleError.Scam)
        }
      }
      "there is an email in the body" in {
        val document =
          ModelBuilder.buildAd(body = "please contact me at contact-trustme@scampro.com for more information")
        AdModeration.checkDocument(document) shouldBe Left(ModerationRuleError.Contact)
      }
      "there is an url in the body" in {
        val document =
          ModelBuilder.buildAd(body = "please check our website http://trustme.scampro.com for more information")
        AdModeration.checkDocument(document) shouldBe Left(ModerationRuleError.Contact)
      }
      "the body makes no sense" in {
        val document = ModelBuilder.buildAd(body = "bdy")
        AdModeration.checkDocument(document) shouldBe Left(ModerationRuleError.Nonsense)
      }

      "when nothing is valid" in {
        val document = ModelBuilder.buildAd(
          body =
            "please check our website http://trustme.scampro.com or contact me at contact-trustme@scampro.com for more information",
          category = miscellaneous,
          price = 100000f
        )
        AdModeration.checkDocument(document) shouldBe Left(ModerationRuleError.Scam)
      }
    }
    "be valid" when {
      "all rules are respected" in {
        val document = ModelBuilder.buildAd()
        AdModeration.checkDocument(document) shouldBe Right(document)
      }
    }
  }
}
