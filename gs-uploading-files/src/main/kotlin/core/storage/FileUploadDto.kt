package core.storage

import java.io.InputStream

class FileUploadDto(val inputStream: InputStream, val fileName: String, val size: Long) {
  fun isEmpty(): Boolean = size == 0L
}