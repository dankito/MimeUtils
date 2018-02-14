package net.dankito.mime

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Test
import java.io.File
import java.net.URI


class MimeTypeDetectorTest {

    private val underTest = MimeTypeDetector()


    @Test
    fun getMimeTypesForFile() {
        val result = underTest.getMimeTypesForFile(File("/tmp/text.txt"))

        assertThat(result?.contains("text/plain"), `is`(true))
    }

    @Test
    fun getMimeTypesForUri() {
        val result = underTest.getMimeTypesForUri(URI.create("http://www.reporter-forum.de/fileadmin/pdf/Reporterpreis_2017/ueberwachung.pdf"))

        assertThat(result?.contains("application/pdf"), `is`(true))
    }

    @Test
    fun getMimeTypesForFilename() {
        val result = underTest.getMimeTypesForFilename("document.docx")

        assertThat(result?.contains("application/vnd.openxmlformats-officedocument.wordprocessingml.document"), `is`(true))
    }

    @Test
    fun getMimeTypesForFilename_UseUri() {
        val result = underTest.getMimeTypesForFilename("http://www.reporter-forum.de/fileadmin/pdf/Reporterpreis_2017/ueberwachung.pdf")

        assertThat(result?.contains("application/pdf"), `is`(true))
    }

    @Test
    fun getMimeTypesForBmpExtension() {
        val result = underTest.getMimeTypesForExtension("bmp")

        assertThat(result?.contains("image/bmp"), `is`(true))
    }

    @Test
    fun getMimeTypesForPngExtension() {
        val result = underTest.getMimeTypesForExtension("png")

        assertThat(result?.contains("image/png"), `is`(true))
    }

    @Test
    fun getMimeTypesForExtension_WithDot() {
        val result = underTest.getMimeTypesForExtension(".png")

        assertThat(result?.contains("image/png"), `is`(true))
    }

    @Test
    fun getMimeTypesForExtension_WithStarAndDot() {
        val result = underTest.getMimeTypesForExtension("*.png")

        assertThat(result?.contains("image/png"), `is`(true))
    }


    @Test
    fun getMimeTypesForExtension_UnknownFileExtension_NoCrashesOccure() {
        val result = underTest.getMimeTypesForExtension(".abcdef")

        assertThat(result, nullValue())
    }

}