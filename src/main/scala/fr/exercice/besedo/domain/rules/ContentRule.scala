package fr.exercice.besedo.domain.rules

import cats.syntax.all.*
import fr.exercice.besedo.domain.error.ModerationRuleError
import fr.exercice.besedo.model.NonEmptyString
import fr.exercice.besedo.model.document.{Document, Subject}

private trait ContentRule {
  protected val minContentValue: Float
  protected val maxContentValue: Float

  protected def contentRule(text: NonEmptyString): Either[ModerationRuleError, Unit] = {
    val lowercaseText  = text.value.toLowerCase()
    val vowels         = Set('a', 'e', 'i', 'o', 'u')
    val consonants     = ('a' to 'z').toSet -- vowels
    val vowelCount     = lowercaseText.count(vowels.contains)
    val consonantCount = lowercaseText.count(consonants.contains)
    val result         = consonantCount.toFloat / (vowelCount + consonantCount).toFloat

    Either.cond(result > minContentValue && result < maxContentValue, (), ModerationRuleError.Nonsense)
  }
}

trait BodyContentRule[T <: Document] extends ContentRule {
  def applyContentRule: T => Either[ModerationRuleError, T] = document => contentRule(document.body).as(document)
}

trait BodyAndSubjectContentRule[T <: Document with Subject] extends ContentRule {
  def applyContentRule: T => Either[ModerationRuleError, T] = document =>
    contentRule(document.body) >> contentRule(document.subject).as(document)
}
