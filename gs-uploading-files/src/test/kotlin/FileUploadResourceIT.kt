import io.dropwizard.testing.junit5.DropwizardAppExtension
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.eclipse.jetty.http.HttpStatus
import org.glassfish.jersey.client.ClientProperties
import org.glassfish.jersey.media.multipart.FormDataMultiPart
import org.glassfish.jersey.media.multipart.MultiPartFeature
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.io.TempDir
import java.io.InputStream
import java.nio.file.Path
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.client.Entity
import javax.ws.rs.core.HttpHeaders
import kotlin.io.path.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@ExtendWith(DropwizardExtensionsSupport::class)
internal class FileUploadResourceIT {
  private val appExtension = DropwizardAppExtension(UploadingFilesApplication::class.java)
  private lateinit var uploadDir: Path

  @BeforeTest
  fun setUp() {
    uploadDir = Path(appExtension.configuration.storage.location)
  }

  @AfterTest
  fun tearDown() {
    uploadDir.listDirectoryEntries()
      .forEach { path ->
        if (path.isDirectory()) path.toFile().deleteRecursively()
        else path.deleteIfExists()
      }
  }

  @Test
  fun `should upload file`(@TempDir tempDir: Path) {
    // DropwizardAppExtension and JerseyClientBuilder clients are not working with multipart, constantly getting 'bad request'.
    val client = ClientBuilder.newBuilder()
      .register(MultiPartFeature::class.java)
      .property(ClientProperties.FOLLOW_REDIRECTS, false)
      .build()
    val file = "Hello, World".toTempFile(tempDir).toFile()
    val fileDataBodyPart = FileDataBodyPart("file", file)

    val response = FormDataMultiPart().use { formDataMultiPart ->
      formDataMultiPart.bodyPart(fileDataBodyPart)
      client.target("http://localhost:${appExtension.localPort}")
        .request()
        .post(Entity.entity(formDataMultiPart, formDataMultiPart.mediaType))
    }

    response.status shouldBe HttpStatus.SEE_OTHER_303
    val uploadedFile = uploadDir.resolve(file.name)
    uploadedFile.exists() shouldBe true
  }

  @Test
  fun `should download file`() {
    val file = "Hello, World".toTempFile(uploadDir).toFile()
    val client = ClientBuilder.newBuilder()
      .register(MultiPartFeature::class.java)
      .build()

    val response = client.target("http://localhost:${appExtension.localPort}/files/${file.name}")
      .request()
      .get()

    response.status shouldBe HttpStatus.OK_200
    response.headers[HttpHeaders.CONTENT_DISPOSITION]?.firstOrNull() shouldBe "attachment; filename=\"${file.name}\""
    response.readEntity(InputStream::class.java)
      .use { `in` ->
        val content = String(`in`.readAllBytes())
        content shouldBe "Hello, World"
      }
  }

  @Test
  fun `should 404 when missing file`() {
    val client = ClientBuilder.newBuilder()
      .register(MultiPartFeature::class.java)
      .build()

    val response = client.target("http://localhost:${appExtension.localPort}/files/foo.txt")
      .request()
      .get()

    response.status shouldBe HttpStatus.NOT_FOUND_404
  }

  @Test
  fun `should list all files`() {
    val file1 = "Hello, World".toTempFile(uploadDir).toFile()
    val file2 = "Hello, World 2".toTempFile(uploadDir).toFile()
    val client = ClientBuilder.newBuilder().build()

    val response = client.target("http://localhost:${appExtension.localPort}")
      .request()
      .get(String::class.java)

    response shouldContain file1.name
    response shouldContain file2.name
  }

  private fun String.toTempFile(dir: Path): Path {
    val tempFile = createTempFile(dir)
    tempFile.writeText(this)
    return tempFile
  }
}