package fr.exercice.besedo.domain.stream

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import com.google.common.jimfs.Jimfs
import fs2.io.file.{Files, Path}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.typelevel.log4cats.slf4j.Slf4jLogger

import java.nio.file.FileSystem
import scala.io.Source

class BatchStreamSpec extends AnyFlatSpec with Matchers with GivenWhenThen {
  it should "return a json file result" in {
    Given("a file system with input and output directory")
    val fileSystem: FileSystem = Jimfs.newFileSystem()
    val outputPath             = Path.fromFsPath(fileSystem, "output")
    val inputPath              = Path.fromFsPath(fileSystem, "input")
    (Files[IO].createDirectory(outputPath) >> Files[IO].createDirectory(inputPath)).unsafeRunSync()

    And("a valid input batch")
    fs2.Stream
      .emits[IO, String](Source.fromResource("input/test.json").getLines().toList)
      .through(Files[IO].writeUtf8(inputPath.resolve("test.json")))
      .compile
      .drain
      .unsafeRunSync()

    When("the stream is executed")
    val stream = new BatchStream(outputPath, Slf4jLogger.create[IO].unsafeRunSync())
    stream.executeBatch(Path.fromFsPath(fileSystem, "input/test.json")).unsafeRunSync()

    Then("a valid output file is written")
    val result         = Files[IO].readUtf8Lines(outputPath.resolve("result.json")).compile.toList.unsafeRunSync()
    val expectedResult = Source.fromResource("output/result-test.json").getLines().toList
    result should contain theSameElementsAs expectedResult
    fileSystem.close()
  }

  it should "fail and stop" in {
    Given("a file system with input and output directory ")
    val fileSystem: FileSystem = Jimfs.newFileSystem()
    val outputPath             = Path.fromFsPath(fileSystem, "output")
    val inputPath              = Path.fromFsPath(fileSystem, "input")
    (Files[IO].createDirectory(outputPath) >> Files[IO].createDirectory(inputPath)).unsafeRunSync()

    And("two invalid input batch")
    fs2.Stream
      .emits[IO, String](Source.fromResource("input/invalidDocument.json").getLines().toList)
      .through(Files[IO].writeUtf8(inputPath.resolve("invalidDocument.json")))
      .compile
      .drain
      .unsafeRunSync()
    fs2.Stream
      .emits[IO, String](Source.fromResource("input/invalidBatch.json").getLines().toList)
      .through(Files[IO].writeUtf8(inputPath.resolve("invalidBatch.json")))
      .compile
      .drain
      .unsafeRunSync()

    When("the stream is executed for one")
    val stream = new BatchStream(outputPath, Slf4jLogger.create[IO].unsafeRunSync())
    stream.executeBatch(inputPath.resolve("invalidDocument.json")).unsafeRunSync()
    stream.executeBatch(inputPath.resolve("invalidBatch.json")).unsafeRunSync()

    Then("no file is found")
    val resultFileExist = Files[IO].readUtf8Lines(outputPath.resolve("result.json")).compile.toList
    assert(resultFileExist.unsafeRunSync().isEmpty)
    fileSystem.close()
  }
}
