import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import core.storage.FileSystemStorageService
import core.storage.IStorageService
import core.storage.StorageConfiguration
import io.dropwizard.Application
import io.dropwizard.forms.MultiPartBundle
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import io.dropwizard.views.ViewBundle
import org.eclipse.jetty.server.session.SessionHandler
import org.glassfish.hk2.utilities.binding.AbstractBinder
import resources.FileUploadResource
import resources.StorageFileNotFoundExceptionMapper
import javax.inject.Singleton

class UploadingFilesApplication : Application<ApplicationConfiguration>() {
  override fun initialize(bootstrap: Bootstrap<ApplicationConfiguration>) {
    bootstrap.objectMapper.registerKotlinModule()
    bootstrap.addBundle(MultiPartBundle())
    bootstrap.addBundle(ViewBundle())
  }

  override fun run(configuration: ApplicationConfiguration, environment: Environment) {
    val storageService = FileSystemStorageService(configuration.storage)
    storageService.deleteAll()
    storageService.init()

    environment.jersey().register(object : AbstractBinder() {
      override fun configure() {
        bind(configuration.storage).to(StorageConfiguration::class.java)
        bindAsContract(FileSystemStorageService::class.java).to(IStorageService::class.java).`in`(Singleton::class.java)
      }
    })
    environment.jersey().register(FileUploadResource::class.java)
    environment.jersey().register(StorageFileNotFoundExceptionMapper::class.java)
    environment.servlets().setSessionHandler(SessionHandler())
  }
}

fun main(args: Array<String>) {
  UploadingFilesApplication().run(*args)
}