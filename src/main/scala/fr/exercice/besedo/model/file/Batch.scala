package fr.exercice.besedo.model.file

import fr.exercice.besedo.model.*
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

final case class Batch(id: NonEmptyAlphanumericString, content: NonEmptyListJson)

object Batch {
  implicit val decoder: Decoder[Batch] = deriveDecoder[Batch]
}
