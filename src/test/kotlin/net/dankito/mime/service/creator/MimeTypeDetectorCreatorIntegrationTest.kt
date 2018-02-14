package net.dankito.mime.service.creator

import net.dankito.mime.service.FileDownloader
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import java.io.File
import java.io.FileReader

class MimeTypeDetectorCreatorIntegrationTest {

    private val downloader = FileDownloader()

    private val underTest = MimeTypeDetectorCreator(EtcMimeTypesFileParser(), JavaMimeUtilsParser(downloader),
            IanaMimeTypeRetriever(IanaMimeTypeFileParser(), downloader), SitePointMimeTypeWebsiteParser(downloader))


    @Test
    fun createMimeTypeDetectorClass() {
        val outputFile = File(File("build", "test"), "MimeTypeDetector.kt")

        underTest.createMimeTypeDetectorClass(outputFile)

        val reader = FileReader(outputFile)

        val createdFile = reader.readText()

        reader.close()

        assertThat(createdFile.lines().size, `is`(3989))
    }

}