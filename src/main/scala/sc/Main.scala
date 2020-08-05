package sc

import cats.effect.{ConcurrentEffect, ExitCode, IO, IOApp, Timer}
import fs2.Stream
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.syntax.kleisli._
import sc.interpreter.TitleHttpInterpreter
import sc.route.{StatusRoute, UrlRoute}
import sc.service.CrawlerService

import scala.concurrent.ExecutionContext.global

object Main extends IOApp {

  def httpApp[F[_] : ConcurrentEffect : Timer]: Stream[F, ExitCode] = {

    val titleRepo = TitleHttpInterpreter[F]()
    val crawlerService = CrawlerService[F](titleRepo)

    val routes = Router[F](
      "/status" -> new StatusRoute[F].routes,
      "/urls" -> new UrlRoute[F](crawlerService).routes
    ).orNotFound

    BlazeServerBuilder[F](global)
      .bindHttp(Config.app.port, Config.app.host)
      .withHttpApp(routes)
      .serve
  }

  override def run(args: List[String]): IO[ExitCode] = {
    httpApp[IO].compile.drain.as(ExitCode.Success)
  }
}