package resources

import api.Greeting
import java.util.concurrent.atomic.AtomicLong
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("")
@Produces(MediaType.APPLICATION_JSON)
class HelloWorldResource {
  private val counter: AtomicLong = AtomicLong()

  @GET
  @Path("/hello-world")
  fun greeting(@QueryParam("name") @DefaultValue("World") name: String) =
    Greeting(counter.incrementAndGet(), "Hello, $name!")
}