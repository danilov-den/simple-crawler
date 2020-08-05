package sc.service

import cats.effect.{Concurrent, Timer}
import fs2.Stream
import sc.Config
import sc.algebra.TitleAlgebra
import sc.model.{Title, TitleError}


class CrawlerService[F[_] : Concurrent : Timer](titleAlgebra: TitleAlgebra[F]) {
  def parallel(urls: List[String]): F[List[Either[TitleError, Title]]] = {
    Stream.emits(urls).covary[F].mapAsync(Config.app.maxConcurrent)(titleAlgebra.getTitle).compile.toList
  }
}

object CrawlerService {
  def apply[F[_] : Concurrent : Timer](titleAlgebra: TitleAlgebra[F]): CrawlerService[F] = new CrawlerService(titleAlgebra)
}