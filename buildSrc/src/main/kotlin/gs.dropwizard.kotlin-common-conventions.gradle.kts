import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm")
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.7.10"))
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("ch.qos.logback:logback-classic:1.2.11")
  implementation("io.github.microutils:kotlin-logging-jvm:2.1.23")
  implementation(platform("io.dropwizard:dropwizard-bom:2.1.1"))
  implementation("io.dropwizard:dropwizard-core")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testImplementation("io.kotest:kotest-assertions-core:5.4.1")
  testImplementation("io.dropwizard:dropwizard-testing")
}

java.sourceCompatibility = JavaVersion.VERSION_17

tasks.withType<KotlinCompile>() {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = JavaVersion.VERSION_17.toString()
  }
}

tasks.named<Test>("test") {
  useJUnitPlatform()
}
