import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import core.redis.RedisBundle
import core.redis.RedisConfig
import io.dropwizard.Application
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment

class MessagingRedisApplication : Application<ApplicationConfig>() {
  override fun initialize(bootstrap: Bootstrap<ApplicationConfig>) {
    bootstrap.objectMapper.registerKotlinModule()
    bootstrap.addBundle(object : RedisBundle<ApplicationConfig>() {
      override fun getRedisConfig(config: ApplicationConfig): RedisConfig = config.redis
    })
  }

  override fun run(configuration: ApplicationConfig, environment: Environment) {
  }
}

fun main(args: Array<String>) {
  MessagingRedisApplication().run(*args)
}