package sc.interpreter

import cats.effect.Sync
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import sc.algebra.TitleAlgebra
import sc.model.{TitleError, Title}

import scala.util.{Failure, Success, Try}

class TitleHttpInterpreter[F[_] : Sync] extends TitleAlgebra[F] {
  def getTitle(url: String): F[Either[TitleError, Title]] = {
    Sync[F].delay {
      Try {
        val browser = JsoupBrowser()
        for {
          doc <- browser.get(url) >?> text("head > title")
        } yield doc
      } match {
        case Success(value) => Right(Title(url, value.getOrElse("")))
        case Failure(exception) => Left(TitleError(url, exception.getMessage))
      }
    }
  }
}

object TitleHttpInterpreter {
  def apply[F[_] : Sync](): TitleHttpInterpreter[F] = new TitleHttpInterpreter()
}