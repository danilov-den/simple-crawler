package sc.algebra

import sc.model.{TitleError, Title}

trait TitleAlgebra[F[_]] {
  def getTitle(url: String): F[Either[TitleError, Title]]
}
