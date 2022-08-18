package core.redis

import io.dropwizard.lifecycle.Managed
import io.lettuce.core.RedisClient
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection
import mu.KotlinLogging

class Sender(private val redisClient: RedisClient, private val receiver: Receiver) : Managed {
  private val logger = KotlinLogging.logger { }

  private var connection: StatefulRedisPubSubConnection<String, String>? = null

  override fun start() {
    connection = redisClient.connectPubSub()
    while (receiver.count == 0) {
      logger.info { "Sending message..." }
      connection?.sync()?.publish("chat", "Hello from Redis!")
      Thread.sleep(500L)
    }
  }

  override fun stop() {
    connection?.close()
  }
}