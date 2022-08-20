import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.dropwizard.Application
import io.dropwizard.Configuration
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import resources.HelloWorldResource

class ActuatorServiceApplication : Application<Configuration>() {
  override fun initialize(bootstrap: Bootstrap<Configuration>) {
    bootstrap.objectMapper.registerKotlinModule()
  }

  override fun run(configuration: Configuration, environment: Environment) {
    environment.jersey().register(HelloWorldResource::class.java)
  }
}

fun main(args: Array<String>) {
  ActuatorServiceApplication().run(*args)
}