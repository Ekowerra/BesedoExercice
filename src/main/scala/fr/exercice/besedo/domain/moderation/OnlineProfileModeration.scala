package fr.exercice.besedo.domain.moderation

import cats.syntax.all.*
import fr.exercice.besedo.domain.error.ModerationRuleError
import fr.exercice.besedo.domain.rules.{AgeRule, BodyAndSubjectContactRule, BodyAndSubjectContentRule}
import fr.exercice.besedo.model.document.Document
import fr.exercice.besedo.model.document.Document.OnlineProfile

object OnlineProfileModeration
    extends Moderation[OnlineProfile]
    with BodyAndSubjectContactRule[OnlineProfile]
    with BodyAndSubjectContentRule[OnlineProfile]
    with AgeRule[OnlineProfile] {
  override protected val minContentValue: Float = 0.1f
  override protected val maxContentValue: Float = 0.84f

  override def checkDocument: OnlineProfile => Either[ModerationRuleError, OnlineProfile] =
    applyAgeRule andThenF applyContactRule andThenF applyContentRule
}
