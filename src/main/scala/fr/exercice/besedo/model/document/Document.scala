package fr.exercice.besedo.model.document

import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.Greater
import fr.exercice.besedo.model.*
import io.circe.Decoder.Result
import io.circe.generic.semiauto.*
import io.circe.{Decoder, HCursor}

sealed trait Document {
  val id: NonEmptyAlphanumericString
  val author: NonEmptyAlphanumericString
  val body: NonEmptyString
}

trait Age {
  val age: Int Refined Greater[0]
}

trait Price {
  val category: AdCategory
  val price: Float Refined Greater[0f]
}

trait Subject {
  val subject: NonEmptyString
}

object Document {
  final case class Ad(
      id: NonEmptyAlphanumericString,
      author: NonEmptyAlphanumericString,
      body: NonEmptyString,
      category: AdCategory,
      price: Float Refined Greater[0f]
  ) extends Document
      with Price

  final case class OnlineProfile(
      id: NonEmptyAlphanumericString,
      author: NonEmptyAlphanumericString,
      body: NonEmptyString,
      gender: Gender,
      seeks: Gender,
      age: Int Refined Greater[0],
      subject: NonEmptyString
  ) extends Document
      with Age
      with Subject

  final case class PrivateMessage(
      id: NonEmptyAlphanumericString,
      author: NonEmptyAlphanumericString,
      body: NonEmptyString,
      subject: NonEmptyString,
      to: NonEmptyAlphanumericString
  ) extends Document
      with Subject

  implicit private val decoderAd: Decoder[Ad]                         = deriveDecoder
  implicit private val decoderOnlineProfile: Decoder[OnlineProfile]   = deriveDecoder
  implicit private val decoderPrivateMessage: Decoder[PrivateMessage] = deriveDecoder

  implicit val decoderDocument: Decoder[Document] = (c: HCursor) =>
    c.downField("type").as[DocumentType] match
      case Left(value) => Left(value)
      case Right(value) =>
        value match
          case DocumentType.classified => c.as[Ad]
          case DocumentType.profile    => c.as[OnlineProfile]
          case DocumentType.message    => c.as[PrivateMessage]

}
