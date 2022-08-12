import core.storage.StorageConfiguration
import io.dropwizard.Configuration

class ApplicationConfiguration(val storage: StorageConfiguration = StorageConfiguration()) : Configuration()