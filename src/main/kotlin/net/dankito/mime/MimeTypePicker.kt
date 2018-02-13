package net.dankito.mime


class MimeTypePicker {

    fun getBestPick(detector: MimeTypeDetector, fileExtension: String): String? {
        return getBestPick(detector.getMimeTypesForExtension(fileExtension))
    }


    fun getBestPick(mimeTypes: List<String>?): String? {
        mimeTypes?.let {
            if(mimeTypes.isEmpty()) {
                return null
            }

            val mimeTypesWithoutEmptyEntries = mimeTypes.filter { it.isNotBlank() }
            if(mimeTypesWithoutEmptyEntries.size == 1) {
                return mimeTypesWithoutEmptyEntries[0]
            }


            val withoutExtensionsMimeTypes = mimeTypesWithoutEmptyEntries.filter { it.contains("x-") == false }
            if(withoutExtensionsMimeTypes.size == 1) {
                return withoutExtensionsMimeTypes[0]
            }


            val withoutApplicationMimeTypes = mimeTypesWithoutEmptyEntries.filter { it.startsWith("application/") == false }
            if(withoutApplicationMimeTypes.size == 1) {
                return withoutApplicationMimeTypes[0]
            }

            val withoutApplicationAndExtensionsMimeTypes = withoutApplicationMimeTypes.filter { it.contains("x-") == false }
            if(withoutApplicationAndExtensionsMimeTypes.size == 1) {
                return withoutApplicationAndExtensionsMimeTypes[0]
            }
            else if(withoutApplicationAndExtensionsMimeTypes.isNotEmpty()) {
                return tryToGuessBestMimeType(withoutApplicationAndExtensionsMimeTypes, mimeTypesWithoutEmptyEntries)
            }

            return mimeTypes.last() // often the last Mime type is the best one
        }

        return null
    }

    private fun tryToGuessBestMimeType(withoutApplicationAndExtensionsMimeTypes: List<String>, mimeTypesWithoutEmptyEntries: List<String>): String {
        getMostUsedCategory(mimeTypesWithoutEmptyEntries)?.let { category ->
            val mimeTypesOfCategory = mimeTypesWithoutEmptyEntries.filter { it.startsWith(category) }
            return mimeTypesOfCategory.sortedBy { it.length }.first()
        }

        getKnownBestPick(withoutApplicationAndExtensionsMimeTypes)?.let { return it }

        return withoutApplicationAndExtensionsMimeTypes.sortedBy { it.length }.first() // the shortest one often is the best one
    }

    private fun getKnownBestPick(mimeTypes: List<String>): String? {
        return when {
            mimeTypes.contains("audio/mpeg3") -> "audio/mpeg3"
            mimeTypes.contains("video/mpeg") -> "video/mpeg"
            mimeTypes.contains("video/3gpp") -> "video/3gpp"
            else -> null
        }
    }

    private fun getMostUsedCategory(mimeTypesWithoutEmptyEntries: List<String>): String? {
        val categories = LinkedHashMap<String, Int>()

        mimeTypesWithoutEmptyEntries.map { it.substringBefore('/') }.forEach { category ->
            val currentCount = categories[category]
            categories[category] = if(currentCount != null) currentCount + 1 else 1
        }

        if(categories.size == 1) {
            return categories.keys.first()
        }

        val sortedCategories = categories.toList().sortedByDescending { (_, count) -> count }.toMap(LinkedHashMap())
        if(sortedCategories[sortedCategories.keys.first()] ?: 0 > sortedCategories[sortedCategories.keys.toList()[1]] ?: 0) {
            return sortedCategories.keys.first()
        }

        return null
    }

}