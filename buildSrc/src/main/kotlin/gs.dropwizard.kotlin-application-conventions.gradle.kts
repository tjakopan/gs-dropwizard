plugins {
  id("gs.dropwizard.kotlin-common-conventions")
  application
}

dependencies {
  implementation(platform("io.dropwizard:dropwizard-bom:2.1.1"))
  implementation("io.dropwizard:dropwizard-core")
//  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

  testImplementation("io.dropwizard:dropwizard-testing")
}