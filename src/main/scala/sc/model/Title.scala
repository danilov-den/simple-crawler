package sc.model

case class Title(
  url: String,
  title: String
)

case class TitleError(
  url: String,
  error: String
)
