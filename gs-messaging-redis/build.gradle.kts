plugins {
  id("gs.dropwizard.kotlin-application-conventions")
}

dependencies {
  implementation("io.lettuce:lettuce-core:6.2.0.RELEASE")
}

application {
  mainClass.set("MessagingRedisApplicationKt")
}