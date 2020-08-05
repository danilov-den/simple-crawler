package sc.route

import cats.effect.Sync
import cats.syntax.functor._
import cats.syntax.flatMap._
import io.circe.generic.auto._
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import sc.model.{TitleError, Title, Titles, Urls}
import sc.service.CrawlerService

class UrlRoute[F[_] : Sync](crawlerService: CrawlerService[F]) extends Http4sDsl[F] {

  implicit val urlsDecoder = jsonOf[F, Urls]
  implicit val urlsEncoder = jsonEncoderOf[F, Urls]
  implicit val titlesEncoder = jsonEncoderOf[F, Titles]

  def makeTitles(titles: List[Either[TitleError, Title]]) = {
    Titles(
      titles.flatMap(_.right.toOption),
      titles.flatMap(_.left.toOption)
    )
  }

  val routes: HttpRoutes[F] =
    HttpRoutes.of[F] {
      case req@POST -> Root => {
        for {
          urls <- req.as[Urls]
          titles <- crawlerService.parallel(urls.urls)
          result <- Ok(makeTitles(titles))
        } yield result
      }
    }
}
