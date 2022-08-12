package resources

import core.storage.StorageFileNotFoundException
import org.eclipse.jetty.http.HttpStatus
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper

class StorageFileNotFoundExceptionMapper : ExceptionMapper<StorageFileNotFoundException> {
  override fun toResponse(exception: StorageFileNotFoundException): Response {
    return Response.status(HttpStatus.NOT_FOUND_404)
      .build()
  }
}