package fr.exercice.besedo

import eu.timepit.refined.api.Refined
import eu.timepit.refined.boolean.{And, Not}
import eu.timepit.refined.collection.{MaxSize, MinSize, NonEmpty}
import eu.timepit.refined.string.MatchesRegex
import io.circe.refined.CirceCodecRefined
import io.circe.{Codec, Decoder, Json}

package object model extends CirceCodecRefined {
  type NonEmptyAlphanumeric       = NonEmpty And MatchesRegex[Alphanumeric]
  type NonEmptyOrWhitespaces      = NonEmpty And Not[MatchesRegex[WhitespacesOnly]]
  type NonEmptyAlphanumericString = Refined[String, NonEmptyAlphanumeric]
  type NonEmptyString             = Refined[String, NonEmptyOrWhitespaces]
  type NonEmptyListJson           = Refined[List[Json], NonEmpty And MinSize[1] And MaxSize[30]]
  private type Alphanumeric       = "^[a-zA-Z0-9]*$"
  private type WhitespacesOnly    = "^[ ]*$"

  implicit val codecNonEmptyAlphanumericString: Codec[NonEmptyAlphanumericString] =
    Codec.from(refinedDecoder, refinedEncoder)
  implicit val decoderNonEmptyString: Decoder[NonEmptyString]       = refinedDecoder
  implicit val decoderNonEmptyListFinite: Decoder[NonEmptyListJson] = refinedDecoder

}
