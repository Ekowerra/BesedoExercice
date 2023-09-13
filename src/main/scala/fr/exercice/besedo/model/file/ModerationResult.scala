package fr.exercice.besedo.model.file

import fr.exercice.besedo.model.{codecNonEmptyAlphanumericString, NonEmptyAlphanumericString}
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

final case class ModerationResult(
    id: NonEmptyAlphanumericString,
    status: ModerationStatus,
    reason: Option[RejectReason]
)

object ModerationResult {
  implicit val encoder: Encoder[ModerationResult] = deriveEncoder
}
