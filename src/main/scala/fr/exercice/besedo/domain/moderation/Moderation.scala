package fr.exercice.besedo.domain.moderation

import fr.exercice.besedo.domain.error.ModerationRuleError
import fr.exercice.besedo.model.document.Document

trait Moderation[T <: Document] {
  def checkDocument: T => Either[ModerationRuleError, T]
}

object Moderation {
  def checkDocument(document: Document): Either[ModerationRuleError, Document] = document match
    case ad: Document.Ad                  => AdModeration.checkDocument(ad)
    case profile: Document.OnlineProfile  => OnlineProfileModeration.checkDocument(profile)
    case message: Document.PrivateMessage => PrivateMessageModeration.checkDocument(message)
}
