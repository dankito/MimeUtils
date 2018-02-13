package net.dankito.mime.service

import net.dankito.mime.IanaMimeTypeFileParser
import net.dankito.mime.IanaMimeTypeRetriever
import net.dankito.mime.util.FileDownloader
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import java.io.File
import java.io.FileReader

class MimeTypeDetectorCreatorIntegrationTest {

    private val underTest = MimeTypeDetectorCreator(IanaMimeTypeRetriever(IanaMimeTypeFileParser(), FileDownloader()))


    @Test
    fun createMimeTypeDetectorClass() {
        val outputFile = File(File("build", "test"), "MimeTypeDetector.kt")

        underTest.createMimeTypeDetectorClass(outputFile)

        val reader = FileReader(outputFile)

        val createdFile = reader.readText()

        reader.close()

        assertThat(createdFile.lines().size, `is`(1735))
    }

}