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
open class MimeTypeDetectorCreator(protected val retriever: IanaMimeTypeRetriever) {


    @JvmOverloads
    open fun createMimeTypeDetectorClass(outputFile: File, config: CreateMimeTypeDetectorConfig = CreateMimeTypeDetectorConfig()) {
        outputFile.parentFile.mkdirs()

        val mimeTypesToExtensionsMap = retriever.retrieveAndParseAllFiles()
        val mimeTypesMap = config.mimeTypesMapVariableName
        val fileExtensionsMap = config.fileExtensionsMapVariableName
        var indent = 0

        val writer = FileWriter(outputFile)


        writePackageDeclaration(writer, indent, config)

        indent = writeClassStatementAndComment(writer, indent, config)


        writeFields(writer, indent, mimeTypesMap, fileExtensionsMap)
        writeEmptyLine(writer)


        indent = writeInitializerMethod(writer, indent, mimeTypesToExtensionsMap)

        writeEmptyLine(writer)


        indent = writeGetMimeTypeForExtension(writer, indent, fileExtensionsMap)

        indent = writeGetExtensionsForMimeTypeMethod(writer, indent, mimeTypesMap)

        writeEmptyLine(writer)


        writeAddMethod(writer, indent, mimeTypesMap, fileExtensionsMap)


        writeLine(writer, "}")

        writer.close()
    }


    open protected fun writePackageDeclaration(writer: FileWriter, indent: Int, config: CreateMimeTypeDetectorConfig) {
        writeLineAndAnEmptyLine(writer, "package ${config.packageName}", indent)
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

        writeLineAndAnEmptyLine(writer, "protected val $fileExtensionsMap = HashMap<String, String>()", indent)
    }

    open protected fun writeInitializerMethod(writer: FileWriter, indent: Int, mimeTypesToExtensionsMap: Map<String, Set<String>>): Int {
        var newIndent = indent

        writeLine(writer, "init {", newIndent)
        newIndent++

        mimeTypesToExtensionsMap.keys.forEach { mimeType ->
            mimeTypesToExtensionsMap[mimeType]?.forEach { fileExtension ->
                writeLine(writer, "add(\"$mimeType\", \"$fileExtension\")", newIndent)
            }
        }

        newIndent = writeStatementEnd(writer, newIndent)

        return newIndent
    }

    open protected fun writeGetMimeTypeForExtension(writer: FileWriter, indent: Int, fileExtensionsMap: String): Int {
        var newIndent = indent

        writeLine(writer, "open fun getMimeTypeForExtension(fileExtension: String): String? {", newIndent)
        newIndent++

        writeLine(writer, "return $fileExtensionsMap[fileExtension.toLowerCase()]", newIndent)

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

        writeLine(writer, "$fileExtensionsMap.put(fileExtension.toLowerCase(), mimeType)", newIndent)

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