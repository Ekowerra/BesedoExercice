package fr.exercice.besedo.domain.stream

import cats.effect.IO
import cats.syntax.all.*
import eu.timepit.refined.api.Refined
import fr.exercice.besedo.domain.moderation.Moderation
import fr.exercice.besedo.model.document.Document
import fr.exercice.besedo.model.file.*
import fr.exercice.besedo.model.{codecNonEmptyAlphanumericString, NonEmptyAlphanumeric}
import fs2.data.json.*
import fs2.data.json.circe.*
import fs2.io.file.{Files, Flags, Path}
import fs2.text
import io.circe.Json
import org.typelevel.log4cats.StructuredLogger

final class BatchStream(outputDirectory: Path, log: StructuredLogger[IO]) {

  def executeBatch(inputFile: Path): IO[Unit] = Files[IO]
    .readUtf8Lines(inputFile)
    .through(tokens[IO, String])
    .through(codec.deserialize[IO, Batch])
    .evalMap(applyModerationToBatch)
    .through(codec.serialize[IO, ModeratedBatch])
    .through(render.pretty())
    .through(text.utf8.encode)
    .through(Files[IO].writeAll(outputDirectory.resolve("result.json"), Flags.Write))
    .compile
    .drain
    .handleErrorWith(error => log.error(error)("a parsing error occurred while stream executed. The stream stopped"))

  private def applyModerationToBatch: Batch => IO[ModeratedBatch] = { case Batch(id, content) =>
    content.value
      .parTraverse(documentJson =>
        documentJson.as[Document] match {
          case Left(error) =>
            extractId(documentJson).flatTap(result =>
              log.warn(s"document ${result.id.value} cannot be parsed. Cause: ${error.message}")
            )
          case Right(document) =>
            applyModeration(document).flatTap(result => log.info(s"document ${result.id.value} has been checked"))
        }
      )
      .map(ModeratedBatch(id, _))
      .flatTap(result => log.info(s"batch ${result.id.value} has been checked"))
  }

  private def applyModeration(document: Document) = IO(Moderation.checkDocument(document)).map {
    case Left(error) =>
      ModerationResult(document.id, ModerationStatus.ko, Some(RejectReason.fromModerationRuleError(error)))
    case Right(_) => ModerationResult(document.id, ModerationStatus.ok, None)
  }

  private def extractId(documentJson: Json): IO[ModerationResult] =
    documentJson.hcursor.downField("id").as[Refined[String, NonEmptyAlphanumeric]] match {
      case Left(_) =>
        IO.raiseError(new Throwable("Stream execute is interrupted. The id of a document was not readable"))
      case Right(id) => IO.pure(ModerationResult(id, ModerationStatus.error, None))
    }
}
