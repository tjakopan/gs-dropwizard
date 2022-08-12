package core.storage

import javax.validation.constraints.NotEmpty

class StorageConfiguration(@NotEmpty val location: String = "upload-dir")