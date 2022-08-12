package core.storage

import java.nio.file.Path

interface IStorageService {
  fun init()
  fun store(file: FileUploadDto)
  fun loadAll(): List<Path>
  fun load(fileName: String): Path
  fun deleteAll()
}