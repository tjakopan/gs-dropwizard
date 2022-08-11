plugins {
  id("gs.dropwizard.kotlin-application-conventions")
}

dependencies {
  implementation("io.dropwizard:dropwizard-client")
}

application {
  mainClass.set("ConsumingRestApplicationKt")
}