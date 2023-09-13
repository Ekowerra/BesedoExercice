package fr.exercice.besedo.domain.moderation

import cats.syntax.all.*
import fr.exercice.besedo.domain.error.ModerationRuleError
import fr.exercice.besedo.domain.rules.{BodyContactRule, BodyContentRule, PriceRule}
import fr.exercice.besedo.model.document.Document
import fr.exercice.besedo.model.document.Document.Ad

object AdModeration extends Moderation[Ad] with BodyContactRule[Ad] with BodyContentRule[Ad] with PriceRule[Ad] {
  override protected val minContentValue: Float = 0.1f
  override protected val maxContentValue: Float = 0.84f

  override def checkDocument: Ad => Either[ModerationRuleError, Ad] =
    applyPriceRule andThenF applyContactRule andThenF applyContentRule
}
