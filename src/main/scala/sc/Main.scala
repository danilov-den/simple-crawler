package sc

import cats.effect.{ConcurrentEffect, ExitCode, IO, IOApp, Timer}
import fs2.Stream
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.syntax.kleisli._
import sc.route.StatusRoute

import scala.concurrent.ExecutionContext.global

object Main extends IOApp {

  def httpApp[F[_] : ConcurrentEffect : Timer]: Stream[F, ExitCode] =
    BlazeServerBuilder[F](global)
      .bindHttp(Config.app.port, Config.app.host)
      .withHttpApp(new StatusRoute[F].routes.orNotFound)
      .serve

  override def run(args: List[String]): IO[ExitCode] =
    httpApp[IO].compile.drain.as(ExitCode.Success)
}