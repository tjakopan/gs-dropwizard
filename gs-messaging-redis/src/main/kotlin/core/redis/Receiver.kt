package core.redis

import io.dropwizard.lifecycle.Managed
import io.lettuce.core.RedisClient
import io.lettuce.core.pubsub.RedisPubSubAdapter
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection
import mu.KotlinLogging
import java.util.concurrent.atomic.AtomicInteger

class Receiver(private val redisClient: RedisClient) : RedisPubSubAdapter<String, String>(), Managed {
  private val logger = KotlinLogging.logger { }

  private var connection: StatefulRedisPubSubConnection<String, String>? = null

  private val counter = AtomicInteger()
  val count: Int
    get() = counter.get()

  override fun start() {
    connection = redisClient.connectPubSub()
    connection?.addListener(this)
    connection?.sync()?.subscribe("chat")
  }

  override fun message(channel: String, message: String) {
    logger.info { "Received <$message>" }
    counter.incrementAndGet()
  }

  override fun stop() {
    connection?.sync()?.unsubscribe("chat")
    connection?.removeListener(this)
    connection?.close()
  }
}