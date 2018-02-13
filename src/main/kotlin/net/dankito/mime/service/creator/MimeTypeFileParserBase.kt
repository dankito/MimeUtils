package net.dankito.mime.service.creator


abstract class MimeTypeFileParserBase {

    open protected fun addFileExtensionForMimeType(mimeTypesToExtensionsMap: HashMap<String, MutableSet<String>>, mimeType: String, fileExtension: String) {
        val fileExtensionsForMimeType = mimeTypesToExtensionsMap[mimeType] ?: LinkedHashSet() // a LinkedHashSet to keep alphabetic ordering

        fileExtensionsForMimeType.add(fileExtension)

        if (fileExtensionsForMimeType.size == 1) {
            mimeTypesToExtensionsMap.put(mimeType, fileExtensionsForMimeType)
        }
    }

}