package net.dankito.mime.service.creator

import net.dankito.mime.service.FileDownloader
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.slf4j.LoggerFactory


/**
 * Parses all Mime types from https://www.sitepoint.com/mime-types-complete-list/.
 *
 * The IANA list for some files doesn't provide the file extensions but a mapping: Mime type -> Mime type.
 * So i added this additional resource for file extension -> Mime type mapping.
 */
open class SitePointMimeTypeWebsiteParser(protected val downloader: FileDownloader) : MimeTypeFileParserBase() {

    companion object {
        const val SitePointMimeTypesWebsiteUrl = "https://www.sitepoint.com/mime-types-complete-list/"

        private val log = LoggerFactory.getLogger(SitePointMimeTypeWebsiteParser::class.java)
    }


    open fun parseSitePointWebsite(): Map<String, MutableSet<String>>? {
        downloader.get(SitePointMimeTypesWebsiteUrl).downloadedContent?.let { html ->
            return parseSitePointWebsite(html)
        }

        return null
    }

    open protected fun parseSitePointWebsite(html: String): Map<String, MutableSet<String>>? {
        val document = Jsoup.parse(html)

        document.body().select("table").forEach { tableElement ->
            if(tableElement.select("thead").html().contains("Media type", true)) { // found table with Mime types
                return parseMediaTypesTable(tableElement)
            }
        }

        return null
    }

    open protected fun parseMediaTypesTable(tableElement: Element): Map<String, MutableSet<String>> {
        val mimeTypesToExtensionsMap = HashMap<String, MutableSet<String>>()

        tableElement.select("tbody tr").forEach { tableRow ->
            val tableData = tableRow.select("td")

            if(tableData.size == 2) {
                addFileExtensionForMimeType(mimeTypesToExtensionsMap, tableData[1].text().trim(), tableData[0].text().trim())
            }
            else {
                log.warn("Table row's column size is ${tableData.size} and not 2:${tableData.mapIndexed { index, element -> "\n[$index] ${element.html()}" } }")
            }
        }

        return mimeTypesToExtensionsMap
    }

}