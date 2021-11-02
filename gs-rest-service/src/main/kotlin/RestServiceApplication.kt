import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.dropwizard.Application
import io.dropwizard.Configuration
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import resources.GreetingResource

class RestServiceApplication : Application<Configuration>() {
  override fun initialize(bootstrap: Bootstrap<Configuration>) {
    bootstrap.objectMapper
      .registerKotlinModule()
  }

  override fun run(configuration: Configuration, environment: Environment) {
    environment.jersey()
      .register(GreetingResource::class.java)
  }
}

fun main(args: Array<String>) {
  RestServiceApplication().run(*args)
}