package sc

import com.typesafe.config.ConfigFactory


object Config {

  private val conf = ConfigFactory.load()

  object app {
    val host: String = conf.getString("app.host")
    val port: Int = conf.getInt("app.port")
  }

}
