package fr.exercice.besedo.domain

import eu.timepit.refined.numeric.Greater
import eu.timepit.refined.refineV
import fr.exercice.besedo.model.document.Document.{Ad, OnlineProfile, PrivateMessage}
import fr.exercice.besedo.model.document.{AdCategory, Gender}
import fr.exercice.besedo.model.{NonEmptyAlphanumeric, NonEmptyOrWhitespaces}
import org.scalatest.EitherValues

object ModelBuilder extends EitherValues {
  def buildAd(
      id: String = "id",
      author: String = "author",
      body: String = "this was a pain cause it actually ask me to write a concrete body",
      category: AdCategory = AdCategory.entertainment,
      price: Float = 300f
  ): Ad = Ad(
    id = refineV[NonEmptyAlphanumeric](id).value,
    author = refineV[NonEmptyAlphanumeric](author).value,
    body = refineV[NonEmptyOrWhitespaces](body).value,
    category = category,
    price = refineV[Greater[0f]](price).value
  )

  def buildOnlineProfile(
      id: String = "id",
      author: String = "author",
      body: String = "this was a pain cause it actually ask me to write a concrete body",
      gender: Gender = Gender.male,
      seeks: Gender = Gender.male,
      age: Int = 25,
      subject: String = "this is a subject"
  ): OnlineProfile = OnlineProfile(
    id = refineV[NonEmptyAlphanumeric](id).value,
    author = refineV[NonEmptyAlphanumeric](author).value,
    body = refineV[NonEmptyOrWhitespaces](body).value,
    gender = gender,
    seeks = seeks,
    age = refineV[Greater[0]](age).value,
    subject = refineV[NonEmptyOrWhitespaces](subject).value
  )

  def buildPrivateMessage(
      id: String = "id",
      author: String = "author",
      body: String = "this was a pain cause it actually ask me to write a concrete body",
      subject: String = "this is a subject",
      to: String = "to"
  ): PrivateMessage = PrivateMessage(
    id = refineV[NonEmptyAlphanumeric](id).value,
    author = refineV[NonEmptyAlphanumeric](author).value,
    body = refineV[NonEmptyOrWhitespaces](body).value,
    subject = refineV[NonEmptyOrWhitespaces](subject).value,
    to = refineV[NonEmptyAlphanumeric](author).value
  )
}
