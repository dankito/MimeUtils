package net.dankito.mime.service.creator

import net.dankito.mime.service.FileDownloader
import org.jsoup.Jsoup
import org.jsoup.nodes.Element


open class JavaMimeUtilsParser(protected val downloader: FileDownloader) : MimeTypeFileParserBase() {

    companion object {
        const val SourceFileUrl = "https://android.googlesource.com/platform/libcore/+/master/luni/src/main/java/libcore/net/MimeUtils.java"
    }


    open fun parseJavaMimeUtilsSource(): Map<String, MutableSet<String>>? {
        downloader.get(SourceFileUrl).downloadedContent?.let { html ->
            return parseJavaMimeUtilsSource(html)
        }

        return null
    }

    open protected fun parseJavaMimeUtilsSource(html: String): Map<String, MutableSet<String>> {
        val mimeTypesToExtensionsMap = HashMap<String, MutableSet<String>>()
        val document = Jsoup.parse(html)

        document.body().select("table.FileContents tbody td.FileContents-lineContents").forEach { sourceCodeLine ->
            if(isMimeTypeToFileExtensionMappingLine(sourceCodeLine)) {
                parseMimeTypeMappingLine(mimeTypesToExtensionsMap, sourceCodeLine)
            }
        }

        return mimeTypesToExtensionsMap
    }

    open protected fun isMimeTypeToFileExtensionMappingLine(sourceCodeLine: Element): Boolean {
        return sourceCodeLine.text().startsWith("add(")
    }

    open protected fun parseMimeTypeMappingLine(mimeTypesToExtensionsMap: HashMap<String, MutableSet<String>>, sourceCodeLine: Element) {
        val stringSpans = sourceCodeLine.select("span.str")
        if(stringSpans.size == 2) {
            val mimeType = stringSpans[0].text().trim().replace("\"", "")
            val fileExtension = stringSpans[1].text().trim().replace("\"", "")

            addFileExtensionForMimeType(mimeTypesToExtensionsMap, mimeType, fileExtension)
        }
    }
}