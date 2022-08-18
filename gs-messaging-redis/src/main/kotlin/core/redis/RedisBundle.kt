package core.redis

import io.dropwizard.Configuration
import io.dropwizard.ConfiguredBundle
import io.dropwizard.setup.Environment
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import mu.KotlinLogging

abstract class RedisBundle<T : Configuration> : ConfiguredBundle<T> {
  private val logger = KotlinLogging.logger {}

  override fun run(configuration: T, environment: Environment) {
    val redisConfig = getRedisConfig(configuration)
    val redisUri = RedisURI.create(redisConfig.uri)
    val redisClient = RedisClient.create(redisUri)
    val receiver = Receiver(redisClient)
    val sender = Sender(redisClient, receiver)

    environment.lifecycle().manage(receiver)
    environment.lifecycle().manage(sender)
  }

  abstract fun getRedisConfig(config: T): RedisConfig
}