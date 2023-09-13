package fr.exercice.besedo

import cats.effect.{IO, IOApp}
import fr.exercice.besedo.config.AppConfig
import fr.exercice.besedo.domain.stream.BatchStream
import fs2.data.json.*
import org.typelevel.log4cats.slf4j.Slf4jLogger

object ApplicationMain extends IOApp.Simple {
  override def run: IO[Unit] =
    Slf4jLogger
      .create[IO]
      .flatMap(log =>
        AppConfig().flatMap { case AppConfig(inputFile, outputDirectory) =>
          val stream = new BatchStream(outputDirectory, log)
          stream.executeBatch(inputFile)
        }
      )
}
