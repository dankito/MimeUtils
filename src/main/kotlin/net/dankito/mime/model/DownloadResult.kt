package net.dankito.mime.model


open class DownloadResult(val isSuccessful: Boolean, val error: Exception? = null, val downloadedContent: String? = null)