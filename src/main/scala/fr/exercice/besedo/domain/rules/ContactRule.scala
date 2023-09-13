package fr.exercice.besedo.domain.rules

import cats.syntax.all.*
import fr.exercice.besedo.domain.error.ModerationRuleError
import fr.exercice.besedo.model.NonEmptyString
import fr.exercice.besedo.model.document.{Document, Subject}

private trait ContactRule {
  protected def contactRule: NonEmptyString => Either[ModerationRuleError, Unit] =
    applyEmailRule andThenF applyUrlRule

  private def applyEmailRule: NonEmptyString => Either[ModerationRuleError, NonEmptyString] = { text =>
    val emailRegex = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b".r
    val emails     = emailRegex.findAllIn(text.value).toList
    Either.cond(emails.isEmpty, text, ModerationRuleError.Contact)
  }

  private def applyUrlRule: NonEmptyString => Either[ModerationRuleError, Unit] = { text =>
    val urlRegex = """https?://\S+""".r
    val urls     = urlRegex.findAllIn(text.value).toList
    Either.cond(urls.isEmpty, (), ModerationRuleError.Contact)
  }
}

trait BodyContactRule[T <: Document] extends ContactRule {
  def applyContactRule: T => Either[ModerationRuleError, T] = document => contactRule(document.body).as(document)
}

trait BodyAndSubjectContactRule[T <: Document with Subject] extends ContactRule {
  def applyContactRule: T => Either[ModerationRuleError, T] = document =>
    contactRule(document.body) >> contactRule(document.subject).as(document)
}
