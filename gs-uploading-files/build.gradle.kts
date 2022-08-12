plugins {
  id("gs.dropwizard.kotlin-application-conventions")
}

dependencies {
  implementation("io.dropwizard:dropwizard-forms")
  implementation("io.dropwizard:dropwizard-views-freemarker")

  testImplementation("io.dropwizard:dropwizard-client")
}

application {
  mainClass.set("UploadingFilesApplicationKt")
}