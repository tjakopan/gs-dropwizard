package resources

import core.storage.FileUploadDto
import core.storage.IStorageService
import io.dropwizard.jersey.sessions.Flash
import io.dropwizard.jersey.sessions.Session
import org.glassfish.jersey.media.multipart.FormDataContentDisposition
import org.glassfish.jersey.media.multipart.FormDataParam
import java.io.InputStream
import java.net.URI
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/")
class FileUploadResource @Inject constructor(private val storageService: IStorageService) {
  @GET
  @Produces(MediaType.TEXT_HTML)
  fun listUploadedFiles(@Session flash: Flash<String>): UploadFormView {
    val files = storageService.loadAll()
      .map { path -> path.fileName.toString() }
    return UploadFormView(files, flash.get().orElse(null))
  }

  @Path("files/{file-name}")
  @GET
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  fun serveFile(@PathParam("file-name") fileName: String): Response {
    val path = storageService.load(fileName)
    return Response.ok(path.toFile())
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$fileName\"")
      .build()
  }

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  fun handleFileUpload(
    @FormDataParam("file") fileInputStream: InputStream,
    @FormDataParam("file") fileContentDisposition: FormDataContentDisposition,
    @Session flash: Flash<String>
  ): Response {
    storageService.store(FileUploadDto(fileInputStream, fileContentDisposition.fileName, fileContentDisposition.size))
    flash.set("You successfully uploaded ${fileContentDisposition.fileName}!")
    return Response.seeOther(URI("/"))
      .build()
  }
}