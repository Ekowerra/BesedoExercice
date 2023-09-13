package fr.exercice.besedo.model.file

import fr.exercice.besedo.model.{codecNonEmptyAlphanumericString, NonEmptyAlphanumericString}
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

final case class ModeratedBatch(id: NonEmptyAlphanumericString, content: List[ModerationResult])

object ModeratedBatch {
  implicit val encoder: Encoder[ModeratedBatch] = deriveEncoder
}
