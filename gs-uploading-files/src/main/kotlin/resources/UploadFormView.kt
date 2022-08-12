package resources

import io.dropwizard.views.View

class UploadFormView(val files: List<String>, val message: String? = null) : View("uploadForm.ftl")