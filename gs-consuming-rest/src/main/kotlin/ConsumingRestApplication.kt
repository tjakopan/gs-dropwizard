import api.Quote
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.dropwizard.Application
import io.dropwizard.Configuration
import io.dropwizard.client.JerseyClientBuilder
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import org.slf4j.LoggerFactory

class ConsumingRestApplication : Application<Configuration>() {
  private val logger = LoggerFactory.getLogger(ConsumingRestApplication::class.java)

  override fun initialize(bootstrap: Bootstrap<Configuration>) {
    bootstrap.objectMapper
      .registerKotlinModule()
  }

  override fun run(configuration: Configuration, environment: Environment) {
    val client = JerseyClientBuilder(environment).build(name)
    val quote = client.target("https://gistcdn.githack.com/ayan-b/ff0441b5a8d6c489b58659ffb849aff4/raw/e1c5ca10f7bea57edd793c4189ea8339de534b45/response.json")
      .request()
      .get(Quote::class.java)
    logger.info(quote.toString())
  }
}

fun main(args: Array<String>) {
  ConsumingRestApplication().run(*args)
}