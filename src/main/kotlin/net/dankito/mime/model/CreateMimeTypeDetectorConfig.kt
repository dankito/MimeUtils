package net.dankito.mime.model


open class CreateMimeTypeDetectorConfig(
        val className: String = DefaultClassName,
        val packageName: String = DefaultPackage,
        val mimeTypePickerVariableName: String = DefaultMimeTypePicker,
        val mimeTypesMapVariableName: String = DefaultMimeTypesToExtensionsMapVariableName,
        val fileExtensionsMapVariableName: String = DefaultFileExtensionsToMimeTypesMapVariableName
) {


    companion object {
        const val DefaultPackage = "net.dankito.mime"

        const val DefaultClassName = "MimeTypeDetector"

        const val DefaultMimeTypePicker = "picker"

        const val DefaultMimeTypesToExtensionsMapVariableName = "mimeTypesMap"

        const val DefaultFileExtensionsToMimeTypesMapVariableName = "fileExtensionsToMimeTypeMap"
    }

}