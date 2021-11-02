plugins {
  application
  kotlin("jvm") version "1.5.31"
}

group = "me.tjakopan"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val dwVersion = "2.0.25"

dependencies {
  implementation(kotlin("stdlib"))
  implementation("io.dropwizard:dropwizard-core:$dwVersion")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")

  testImplementation(kotlin("test"))
  testImplementation("io.dropwizard:dropwizard-testing:$dwVersion")
}

tasks.test {
  useJUnitPlatform()
}