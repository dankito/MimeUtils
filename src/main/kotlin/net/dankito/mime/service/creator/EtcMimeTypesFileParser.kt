package net.dankito.mime.service.creator

import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileReader


/**
 * Parses a Unix /etc/mime.types file.
 */
open class EtcMimeTypesFileParser : MimeTypeFileParserBase() {

    companion object {
        private val log = LoggerFactory.getLogger(EtcMimeTypesFileParser::class.java)
    }


    open fun parseEtcMimeTypesFile(): Map<String, MutableSet<String>> {
        val resourceUrl = EtcMimeTypesFileParser::class.java.classLoader.getResource(File("ect-mime.types").path)

        return parseEtcMimeTypesFile(File(resourceUrl.toURI()))
    }

    open fun parseEtcMimeTypesFile(etcMimeTypesFile: File): Map<String, MutableSet<String>> {
        val mimeTypesToExtensionsMap = HashMap<String, MutableSet<String>>()

        val reader = FileReader(etcMimeTypesFile)

        reader.forEachLine { line ->
            if(isCommentOrBlank(line) == false) {
                parseEtcMimeTypesLines(mimeTypesToExtensionsMap, line)
            }
        }

        reader.close()

        return mimeTypesToExtensionsMap
    }

    open protected fun isCommentOrBlank(line: String): Boolean {
        return line.isBlank() || line.trim().startsWith('#')
    }

    open protected fun parseEtcMimeTypesLines(mimeTypesToExtensionsMap: HashMap<String, MutableSet<String>>, line: String) {
        val columns = line.split('\t').filter { it.isNotBlank() } // Mime type and file extension(s) are separated by \t

        if(columns.size > 1) {
            val mimeType = columns[0].trim()

            val extensions = ArrayList(columns[1].split(' ').filter { it.isNotBlank() }) // file extensions are separated by ' '
            for(i in 2..columns.size - 1) extensions.add(columns[i])

            extensions.forEach { fileExtension ->
                addFileExtensionForMimeType(mimeTypesToExtensionsMap, mimeType, fileExtension.trim())
            }
        }
        else { // there are really a lot of Mime types for which no file extension is set
            log.info("Line contains only one column: $line")
        }
    }

}