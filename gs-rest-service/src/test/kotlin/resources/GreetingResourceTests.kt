package resources

import RestServiceApplication
import api.Greeting
import io.dropwizard.testing.junit5.DropwizardAppExtension
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import javax.ws.rs.client.Client
import kotlin.test.assertEquals

@ExtendWith(DropwizardExtensionsSupport::class)
class GreetingResourceTests {
  private val appExtension = DropwizardAppExtension(RestServiceApplication::class.java)

  private lateinit var client: Client
  private lateinit var uri: String

  @BeforeEach
  fun setUp() {
    client = appExtension.client()
    uri = "http://localhost:${appExtension.localPort}/greeting"
  }

  @Test
  fun `no param greeting should return default message`() {
    val greeting = client.target(uri)
      .request()
      .get(Greeting::class.java)

    assertEquals("Hello, World!", greeting.content)
  }

  @Test
  fun `param greeting should return tailored message`() {
    val greeting = client.target(uri)
      .queryParam("name", "Dropwizard Community")
      .request()
      .get(Greeting::class.java)

    assertEquals("Hello, Dropwizard Community!", greeting.content)
  }
}