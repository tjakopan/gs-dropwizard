package core.storage

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import javax.inject.Inject
import kotlin.io.path.Path
import kotlin.io.path.absolute
import kotlin.io.path.exists
import kotlin.io.path.isReadable

class FileSystemStorageService @Inject constructor(config: StorageConfiguration) : IStorageService {
  private val rootLocation: Path = Path(config.location)

  override fun init() {
    try {
      Files.createDirectories(rootLocation)
    } catch (e: IOException) {
      throw StorageException("Could not initialize storage.", e)
    }
  }

  override fun store(file: FileUploadDto) {
    try {
      if (file.isEmpty()) throw StorageException("Failed to store empty file.")
      val destFile = rootLocation.resolve(Path(file.fileName)).normalize().absolute()
      // This is a security check.
      if (destFile.parent != rootLocation.absolute())
        throw StorageException("Cannot store file outside of current directory.")
      file.inputStream.use { `in` -> Files.copy(`in`, destFile, StandardCopyOption.REPLACE_EXISTING) }
    } catch (e: IOException) {
      throw StorageException("Failed to store file.", e)
    }
  }

  override fun loadAll(): List<Path> {
    try {
      return Files.walk(rootLocation, 1)
        .filter { path -> path != rootLocation }
        .map(rootLocation::relativize)
        .toList()
    } catch (e: IOException) {
      throw StorageException("Failed to read stored files.", e)
    }
  }

  override fun load(fileName: String): Path {
    try {
      val file = rootLocation.resolve(fileName)
      if (file.exists() || file.isReadable()) return file
      else throw StorageFileNotFoundException("Could not read file $fileName.")
    } catch (e: Exception) {
      throw StorageFileNotFoundException("Could not read file $fileName.", e)
    }
  }

  override fun deleteAll() {
    rootLocation.toFile().deleteRecursively()
  }
}