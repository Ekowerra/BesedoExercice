package fr.exercice.besedo.config

import cats.effect.IO
import cats.syntax.parallel.*
import ciris.{env, ConfigDecoder}
import fs2.io.file.Path

import scala.util.Try

final case class AppConfig(inputFile: Path, outputDirectory: Path)

object AppConfig {
  implicit val pathDecoder: ConfigDecoder[String, Path] =
    ConfigDecoder[String, String].mapOption("Path")(path => Try(Path(path)).toOption)

  def apply(): IO[AppConfig] =
    (env("INPUT_FILE").as[Path], env("OUTPUT_DIRECTORY").as[Path]).parMapN(AppConfig.apply).load[IO]
}
