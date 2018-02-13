package net.dankito.mime.service.creator

import net.dankito.mime.model.CreateMimeTypeDetectorConfig
import java.io.File
import java.io.FileWriter
import java.io.Writer
import java.text.SimpleDateFormat
import java.util.*


/**
 * Fetches Mime types files with IanaMimeTypeRetriever
 * and creates a source code file listing all these Mime types.
 */
open class MimeTypeDetectorCreator(protected val ianaMimeTypeRetriever: IanaMimeTypeRetriever, protected val sitePointParser: SitePointMimeTypeWebsiteParser) {


    @JvmOverloads
    open fun createMimeTypeDetectorClass(outputFile: File, config: CreateMimeTypeDetectorConfig = CreateMimeTypeDetectorConfig()) {
        outputFile.parentFile.mkdirs()

        val ianaMimeTypesToExtensionsMap = ianaMimeTypeRetriever.retrieveAndParseAllFiles()
        val sitePointMimeTypesToExtensionsMap = sitePointParser.parseSitePointWebsite()

        val mimeTypesMap = config.mimeTypesMapVariableName
        val fileExtensionsMap = config.fileExtensionsMapVariableName
        var indent = 0

        val writer = FileWriter(outputFile)


        writePackageDeclaration(writer, indent, config)

        writeImports(writer, indent)

        indent = writeClassStatementAndComment(writer, indent, config)


        writeFields(writer, indent, mimeTypesMap, fileExtensionsMap)
        writeEmptyLine(writer)


        indent = writeInitializerMethod(writer, indent, ianaMimeTypesToExtensionsMap, sitePointMimeTypesToExtensionsMap)

        writeEmptyLine(writer)


        indent = writeGetMimeTypesForUriMethod(writer, indent)

        indent = writeGetMimeTypesForFileMethod(writer, indent)

        indent = writeGetMimeTypesForFilenameMethod(writer, indent)

        indent = writeGetMimeTypesForExtensionMethod(writer, indent, fileExtensionsMap)

        indent = writeNormalizeFileExtensionMethod(writer, indent)

        writeEmptyLine(writer)


        indent = writeGetExtensionsForMimeTypeMethod(writer, indent, mimeTypesMap)

        writeEmptyLine(writer)


        writeAddMethod(writer, indent, mimeTypesMap, fileExtensionsMap)


        writeLine(writer, "}")

        writer.close()
    }


    open protected fun writePackageDeclaration(writer: FileWriter, indent: Int, config: CreateMimeTypeDetectorConfig) {
        writeLineAndAnEmptyLine(writer, "package ${config.packageName}", indent)
    }

    open protected fun writeImports(writer: FileWriter, indent: Int) {
        writeLine(writer, "import java.io.File", indent)

        writeLineAndAnEmptyLine(writer, "import java.net.URI", indent)
    }

    open protected fun writeClassStatementAndComment(writer: FileWriter, indent: Int, config: CreateMimeTypeDetectorConfig): Int {
        var newIndent = indent
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss zZ")
        dateFormat.timeZone = TimeZone.getTimeZone("GMT")

        writeLine(writer, "/**", indent)
        writeLine(writer, " * Class containing all IANA Mime types", indent)
        writeLine(writer, " * ", indent)
        writeLine(writer, " * Has methods to retrieve a Mime type from a file extension or", indent)
        writeLine(writer, " * a file extension from a Mime type.", indent)
        writeLine(writer, " * ", indent)
        writeLine(writer, " * Created on ${dateFormat.format(Date())} from the .csv files from", indent)
        writeLine(writer, " * http://www.iana.org/assignments/media-types/media-types.xhtml", indent)
        writeLine(writer, " */", indent)

        writeLineAndAnEmptyLine(writer, "open class ${config.className} {", newIndent)
        newIndent++

        return newIndent
    }

    open protected fun writeFields(writer: FileWriter, indent: Int, mimeTypesMap: String, fileExtensionsMap: String) {
        writeLineAndAnEmptyLine(writer, "protected val $mimeTypesMap = HashMap<String, MutableSet<String>>()", indent)

        writeLineAndAnEmptyLine(writer, "protected val $fileExtensionsMap = HashMap<String, MutableSet<String>>()", indent)
    }

    open protected fun writeInitializerMethod(writer: FileWriter, indent: Int, ianaMimeTypesToExtensionsMap: Map<String, Set<String>>, sitePointMimeTypesToExtensionsMap: Map<String, MutableSet<String>>?): Int {
        var newIndent = indent

        writeLine(writer, "init {", newIndent)
        newIndent++

        ianaMimeTypesToExtensionsMap.keys.forEach { mimeType ->
            ianaMimeTypesToExtensionsMap[mimeType]?.forEach { fileExtension ->
                writeLine(writer, "add(\"$mimeType\", \"$fileExtension\")", newIndent)
            }
        }

        sitePointMimeTypesToExtensionsMap?.let {
            sitePointMimeTypesToExtensionsMap.keys.forEach { mimeType ->
                sitePointMimeTypesToExtensionsMap[mimeType]?.forEach { fileExtension ->
                    writeLine(writer, "add(\"$mimeType\", \"$fileExtension\")", newIndent)
                }
            }
        }

        newIndent = writeStatementEnd(writer, newIndent)

        return newIndent
    }


    open protected fun writeGetMimeTypesForUriMethod(writer: FileWriter, indent: Int): Int {
        var newIndent = indent

        writeLine(writer, "open fun getMimeTypesForUri(uri: URI): List<String>? {", newIndent)
        newIndent++

        writeLine(writer, "return getMimeTypesForFilename(uri.toASCIIString())", newIndent)

        newIndent = writeStatementEnd(writer, newIndent)

        return newIndent
    }

    open protected fun writeGetMimeTypesForFileMethod(writer: FileWriter, indent: Int): Int {
        var newIndent = indent

        writeLine(writer, "open fun getMimeTypesForFile(file: File): List<String>? {", newIndent)
        newIndent++

        writeLine(writer, "return getMimeTypesForExtension(file.extension)", newIndent)

        newIndent = writeStatementEnd(writer, newIndent)

        return newIndent
    }

    open protected fun writeGetMimeTypesForFilenameMethod(writer: FileWriter, indent: Int): Int {
        var newIndent = indent

        writeLine(writer, "open fun getMimeTypesForFilename(filename: String): List<String>? {", newIndent)
        newIndent++

        writeLine(writer, "return getMimeTypesForExtension(filename.substringAfterLast('.', \"\"))", newIndent)

        newIndent = writeStatementEnd(writer, newIndent)

        return newIndent
    }

    open protected fun writeGetMimeTypesForExtensionMethod(writer: FileWriter, indent: Int, fileExtensionsMap: String): Int {
        var newIndent = indent

        writeLine(writer, "open fun getMimeTypesForExtension(fileExtension: String): List<String>? {", newIndent)
        newIndent++

        writeLine(writer, "return $fileExtensionsMap[normalizeFileExtension(fileExtension)]?.toList()", newIndent)

        newIndent = writeStatementEnd(writer, newIndent)

        return newIndent
    }

    open protected fun writeNormalizeFileExtensionMethod(writer: FileWriter, indent: Int): Int {
        var newIndent = indent

        writeLine(writer, "/**", newIndent)
        writeLine(writer, " * Removes '*.' at start of extension filter and lower cases extension", newIndent)
        writeLine(writer, " */", newIndent)

        writeLine(writer, "open protected fun normalizeFileExtension(extension: String?): String? {", newIndent)
        newIndent++

        writeLine(writer, "if(extension == null) {", newIndent)
        newIndent++

        writeLine(writer, "return extension", newIndent)

        newIndent = writeStatementEnd(writer, newIndent)


        writeLine(writer, "var normalizedExtension = extension", newIndent)

        writeLine(writer, "if(normalizedExtension.startsWith('*')) {", newIndent)
        newIndent++

        writeLine(writer, "normalizedExtension = normalizedExtension.substring(1)", newIndent)

        newIndent = writeStatementEnd(writer, newIndent)


        writeLine(writer, "if(normalizedExtension.startsWith('.')) {", newIndent)
        newIndent++

        writeLine(writer, "normalizedExtension = normalizedExtension.substring(1)", newIndent)

        newIndent = writeStatementEnd(writer, newIndent)


        writeLine(writer, "return normalizedExtension.toLowerCase()", newIndent)


        newIndent = writeStatementEnd(writer, newIndent)

        return newIndent
    }


    open protected fun writeGetExtensionsForMimeTypeMethod(writer: FileWriter, indent: Int, mimeTypesMap: String): Int {
        var newIndent = indent

        writeLine(writer, "open fun getExtensionsForMimeType(mimeType: String): List<String>? {", newIndent)
        newIndent++

        writeLine(writer, "return $mimeTypesMap[mimeType.toLowerCase()]?.toList()", newIndent)

        newIndent = writeStatementEnd(writer, newIndent)

        return newIndent
    }

    open protected fun writeAddMethod(writer: FileWriter, indent: Int, mimeTypesMap: String, fileExtensionsMap: String): Int {
        var newIndent = indent

        writeLine(writer, "open protected fun add(mimeType: String, fileExtension: String) {", newIndent)
        newIndent++

        writeLineAndAnEmptyLine(writer, "val mimeTypeLowerCased = mimeType.toLowerCase()", newIndent)

        writeLine(writer, "if($mimeTypesMap.containsKey(mimeTypeLowerCased) == false) {", newIndent)
        newIndent++

        writeLine(writer, "$mimeTypesMap.put(mimeTypeLowerCased, LinkedHashSet())", newIndent)

        newIndent = writeStatementEnd(writer, newIndent)

        writeLineAndAnEmptyLine(writer, "$mimeTypesMap[mimeTypeLowerCased]?.add(fileExtension)", newIndent)


        writeLineAndAnEmptyLine(writer, "val fileExtensionLowerCased = fileExtension.toLowerCase()", newIndent)

        writeLine(writer, "if($fileExtensionsMap.containsKey(fileExtensionLowerCased) == false) {", newIndent)
        newIndent++

        writeLine(writer, "$fileExtensionsMap.put(fileExtensionLowerCased, LinkedHashSet())", newIndent)

        newIndent = writeStatementEnd(writer, newIndent)

        writeLine(writer, "$fileExtensionsMap[fileExtensionLowerCased]?.add(mimeType)", newIndent)

        newIndent = writeStatementEnd(writer, newIndent)

        return newIndent
    }


    open protected fun writeLine(writer: Writer, line: String, indent: Int = 0) {
        val indentString = "\t".repeat(indent)

        writer.write(indentString + line + getLineSeparator())
    }

    open protected fun writeLineAndAnEmptyLine(writer: Writer, line: String, indent: Int = 0) {
        writeLine(writer, line, indent)

        writeEmptyLine(writer)
    }

    open protected fun writeEmptyLine(writer: Writer) {
        writeLine(writer, "")
    }

    open protected fun writeStatementEnd(writer: Writer, indent: Int): Int {
        val newIndent = indent - 1

        writeLine(writer, "}", newIndent)

        writeEmptyLine(writer)

        return newIndent
    }

    open protected fun getLineSeparator() = System.lineSeparator()

}