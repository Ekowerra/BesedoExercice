package fr.exercice.besedo.domain.moderation

import cats.syntax.all.*
import fr.exercice.besedo.domain.error.ModerationRuleError
import fr.exercice.besedo.domain.moderation.OnlineProfileModeration.applyContactRule
import fr.exercice.besedo.domain.rules.{
  AgeRule,
  BodyAndSubjectContactRule,
  BodyAndSubjectContentRule,
  ContactRule,
  ContentRule
}
import fr.exercice.besedo.model.document.Document
import fr.exercice.besedo.model.document.Document.PrivateMessage

object PrivateMessageModeration
    extends Moderation[PrivateMessage]
    with BodyAndSubjectContactRule[PrivateMessage]
    with BodyAndSubjectContentRule[PrivateMessage] {
  override protected val minContentValue: Float = 0.2f
  override protected val maxContentValue: Float = 0.76f

  override def checkDocument: PrivateMessage => Either[ModerationRuleError, PrivateMessage] =
    applyContactRule andThenF applyContentRule

}
