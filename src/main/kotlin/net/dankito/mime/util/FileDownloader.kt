package net.dankito.mime.util

import net.dankito.mime.model.DownloadResult
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit


open class FileDownloader {

    companion object {
        const val DefaultConnectionTimeoutMillis = 2000

        const val DefaultCountConnectionRetries = 2

        private val log = LoggerFactory.getLogger(FileDownloader::class.java)
    }


    // avoid creating several instances, should be singleton
    private val client: OkHttpClient


    init {
        val builder = OkHttpClient.Builder()

        builder.followRedirects(true)
        builder.retryOnConnectionFailure(true)
        builder.connectTimeout(DefaultConnectionTimeoutMillis.toLong(), TimeUnit.MILLISECONDS) // TODO: find a way to set per call
        builder.readTimeout(DefaultConnectionTimeoutMillis.toLong(), TimeUnit.MILLISECONDS)
        builder.writeTimeout(DefaultConnectionTimeoutMillis.toLong(), TimeUnit.MILLISECONDS)

        client = builder.build()
    }


    open fun getAsync(url: String, callback: (DownloadResult) -> Unit) {
        callback(get(url))
    }

    open fun get(url: String): DownloadResult {
        try {
            log.info("Starting to download from url $url")

            val request = Request.Builder().url(url).build()

            val response = executeRequest(request)

            log.info("Was downloading $url successful? ${response.isSuccessful}")
            return DownloadResult(true, downloadedContent = response.body()?.string())
        } catch(e: Exception) {
            log.error("Could not download url $url", e)

            return DownloadResult(false, e)
        }
    }


    @Throws(Exception::class)
    open protected fun executeRequest(request: Request, countRetries: Int = DefaultCountConnectionRetries): Response {
        val response = client.newCall(request).execute()

        if(response.isSuccessful == false && countRetries > 0) {
            return executeRequest(request, countRetries - 1)
        }
        else {
            return response
        }
    }
}