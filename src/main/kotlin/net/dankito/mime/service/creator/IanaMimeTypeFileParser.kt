package net.dankito.mime.service.creator

import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileReader


open class IanaMimeTypeFileParser {

    companion object {
        private val log = LoggerFactory.getLogger(IanaMimeTypeFileParser::class.java)
    }


    open fun parseIanaCsvFileNotThrowingExceptions(csvFile: File): Map<String, MutableSet<String>>? {
        try {
            return parseIanaCsvFile(csvFile)
        } catch(e: Exception) {
            log.error("Could not parse .csv file $csvFile", e)
        }

        return null
    }

    open fun parseIanaCsvFile(csvFile: File): Map<String, MutableSet<String>> {
        val reader = FileReader(csvFile)

        val fileContent = reader.readLines()

        reader.close()

        return parseIanaCsvFile(fileContent)
    }

    open fun parseIanaCsvFile(csvFileContent: String): Map<String, MutableSet<String>> {
        return parseIanaCsvFile(csvFileContent.lines())
    }

    open fun parseIanaCsvFile(csvFileLines: List<String>): Map<String, MutableSet<String>> {
        val mimeTypesToExtensionsMap = HashMap<String, MutableSet<String>>()

        csvFileLines.forEach { line ->
            if(line.isNotBlank() && isHeaderLine(line) == false) {
                parseCsvLine(mimeTypesToExtensionsMap, line)
            }
        }

        return mimeTypesToExtensionsMap
    }

    open protected fun parseCsvLine(mimeTypesToExtensionsMap: HashMap<String, MutableSet<String>>, line: String) {
        val columns = line.split(',')

        if(isValidLine(columns)) {
            val fileExtension = columns[0]
            val mimeType = columns[1] // TODO: what to do with empty Mime types?

            addFileExtensionForMimeType(mimeTypesToExtensionsMap, mimeType, fileExtension)
        }
        else {
            log.warn("Csv line has ${columns.size} but should have 3 or for image.csv 4 columns: $line")
        }
    }

    open protected fun addFileExtensionForMimeType(mimeTypesToExtensionsMap: HashMap<String, MutableSet<String>>, mimeType: String, fileExtension: String) {
        val fileExtensionsForMimeType = mimeTypesToExtensionsMap[mimeType] ?: LinkedHashSet() // a LinkedHashSet to keep alphabetic ordering

        fileExtensionsForMimeType.add(fileExtension)

        if (fileExtensionsForMimeType.size == 1) {
            mimeTypesToExtensionsMap.put(mimeType, fileExtensionsForMimeType)
        }
    }

    open protected fun isHeaderLine(line: String): Boolean {
        return line.toLowerCase().startsWith("name,")
    }

    open protected fun isValidLine(columns: List<String>): Boolean {
        if(columns.size == 3 || columns.size == 4) { // image.csv contains as third column in some cases a description
            return columns[0].isNotBlank() /*  && columns[1].isNotBlank() */ // Mime type to an extensions is not always set
        }

        return false
    }

}