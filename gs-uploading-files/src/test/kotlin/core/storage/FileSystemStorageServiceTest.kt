package core.storage

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowWithMessage
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.io.path.exists
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class FileSystemStorageServiceTest {
  private lateinit var config: StorageConfiguration
  private lateinit var service: FileSystemStorageService

  @BeforeTest
  fun setUp(@TempDir tempDir: File) {
    config = StorageConfiguration(tempDir.absolutePath)
    service = FileSystemStorageService(config)
  }

  @Test
  fun `load non existent`() {
    shouldThrow<StorageFileNotFoundException> { service.load("foo.txt") }
  }

  @Test
  fun `save and load`() {
    service.store("Hello, World".toFileUploadDto("foo.txt"))
    val path = service.load("foo.txt")

    path.exists() shouldBe true
  }

  @Test
  fun `save relative path not permitted`() {
    shouldThrowWithMessage<StorageException>("Cannot store file outside of current directory.") {
      service.store("Hello, World".toFileUploadDto("../foo.txt"))
    }
  }

  @Test
  fun `save absolute path not permitted`() {
    shouldThrowWithMessage<StorageException>("Cannot store file outside of current directory.") {
      service.store("Hello, World".toFileUploadDto("/etc/passwd"))
    }
  }

  @Test
  fun `save permitted`() {
    service.store("Hello, World".toFileUploadDto("bar/../foo.txt"))
  }

  private fun String.toFileUploadDto(fileName: String): FileUploadDto {
    val bytes = this.toByteArray()
    return FileUploadDto(bytes.inputStream(), fileName, bytes.size.toLong())
  }
}