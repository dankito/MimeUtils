package net.dankito.mime

import net.dankito.mime.service.FileDownloader
import java.io.File


open class IanaMimeTypeRetriever(protected val parser: IanaMimeTypeFileParser, protected val downloader: FileDownloader) {

    companion object {
        const val ApplicationCsvUrl = "http://www.iana.org/assignments/media-types/application.csv"

        const val AudioCsvUrl = "http://www.iana.org/assignments/media-types/audio.csv"

        const val FontCsvUrl = "http://www.iana.org/assignments/media-types/font.csv"

        const val ImageCsvUrl = "http://www.iana.org/assignments/media-types/image.csv"

        const val MessageCsvUrl = "http://www.iana.org/assignments/media-types/message.csv"

        const val ModelCsvUrl = "http://www.iana.org/assignments/media-types/model.csv"

        const val MultipartCsvUrl = "http://www.iana.org/assignments/media-types/multipart.csv"

        const val TextCsvUrl = "http://www.iana.org/assignments/media-types/text.csv"

        const val VideoCsvUrl = "http://www.iana.org/assignments/media-types/video.csv"

        val CsvFilesUrls = listOf(ApplicationCsvUrl, AudioCsvUrl, FontCsvUrl, ImageCsvUrl, MessageCsvUrl,
                ModelCsvUrl, MultipartCsvUrl, TextCsvUrl, VideoCsvUrl)
    }


    open fun retrieveAllFiles(): Map<String, String?> {
        val fileDownloadResults = HashMap<String, String?>()

        CsvFilesUrls.forEach { csvFileUrl ->
            val result = downloader.get(csvFileUrl)

            fileDownloadResults.put(File(csvFileUrl).name, result.downloadedContent)
        }

        return fileDownloadResults
    }


    open fun retrieveAndParseAllFiles(): Map<String, Set<String>> {
        val mimeTypesToExtensionsMap = HashMap<String, MutableSet<String>>()

        retrieveAllFiles().values.filterNotNull().forEach { csvFileContent ->
            val mimeTypesToExtensionsMapForFile = parser.parseIanaCsvFile(csvFileContent)

            mergeMimeTypes(mimeTypesToExtensionsMap, mimeTypesToExtensionsMapForFile)
        }

        return mimeTypesToExtensionsMap
    }

    open protected fun mergeMimeTypes(mimeTypesToExtensionsMap: HashMap<String, MutableSet<String>>, mimeTypesToExtensionsMapForFile: Map<String, MutableSet<String>>) {
        mimeTypesToExtensionsMapForFile.forEach { mimeType, fileExtensions ->
            if(mimeTypesToExtensionsMap.containsKey(mimeType)) { // for file extensions without Mime type
                mimeTypesToExtensionsMap[mimeType]?.addAll(fileExtensions)
            }
            else {
                mimeTypesToExtensionsMap.put(mimeType, fileExtensions)
            }
        }
    }

}