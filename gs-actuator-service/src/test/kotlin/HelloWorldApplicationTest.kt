import io.dropwizard.testing.junit5.DropwizardAppExtension
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.extension.ExtendWith
import javax.ws.rs.client.Client
import kotlin.test.BeforeTest
import kotlin.test.Test

@ExtendWith(DropwizardExtensionsSupport::class)
class HelloWorldApplicationTest {
  private val appExtension = DropwizardAppExtension(ActuatorServiceApplication::class.java)
  private lateinit var client: Client
  private var localPort: Int = 0
  private var adminPort: Int = 0

  @BeforeTest
  fun setUp() {
    client = appExtension.client()
    localPort = appExtension.localPort
    adminPort = appExtension.adminPort
  }

  @Test
  fun `should return 200 when sending request to controller`() {
    val response = client.target("http://localhost:$localPort/hello-world").request().get()

    response.status shouldBe 200
  }

  @Test
  fun `should return 200 when sending request to health endpoint`() {
    val response = client.target("http://localhost:$adminPort/healthcheck").request().get()

    response.status shouldBe 200
  }

  @Test
  fun `should return 200 when sending request to metrics endpoint`() {
    val response = client.target("http://localhost:$adminPort/metrics").request().get()

    response.status shouldBe 200
  }
}