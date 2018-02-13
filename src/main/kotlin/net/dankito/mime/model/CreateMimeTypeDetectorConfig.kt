package net.dankito.mime.model


open class CreateMimeTypeDetectorConfig(
        val className: String = DefaultClassName,
        val packageName: String = DefaultPackage,
        val mimeTypesMapVariableName: String = DefaultMimeTypesToExtensionsMapVariableName,
        val fileExtensionsMapVariableName: String = DefaultFileExtensionsToMimeTypesMapVariableName
) {


    companion object {
        const val DefaultPackage = "net.dankito.mime"

        const val DefaultClassName = "MimeTypeDetector"

        const val DefaultMimeTypesToExtensionsMapVariableName = "mimeTypesMap"

        const val DefaultFileExtensionsToMimeTypesMapVariableName = "fileExtensionsToMimeTypeMap"
    }

}