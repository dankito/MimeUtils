package net.dankito.mime

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import java.io.File
import java.net.URI


class MimeTypeDetectorTest {

    private val underTest = MimeTypeDetector()


    @Test
    fun getMimeTypeForFile() {
        val result = underTest.getMimeTypeForFile(File("/tmp/text.txt"))

        assertThat(result, `is`("text/plain"))
    }

    @Test
    fun getMimeTypeForUri() {
        val result = underTest.getMimeTypeForUri(URI.create("http://www.reporter-forum.de/fileadmin/pdf/Reporterpreis_2017/ueberwachung.pdf"))

        assertThat(result, `is`("application/pdf"))
    }

    @Test
    fun getMimeTypeForFilename() {
        val result = underTest.getMimeTypeForFilename("document.docx")

        assertThat(result, `is`("application/pdf"))
    }

    @Test
    fun getMimeTypeForFilename_UseUri() {
        val result = underTest.getMimeTypeForFilename("http://www.reporter-forum.de/fileadmin/pdf/Reporterpreis_2017/ueberwachung.pdf")

        assertThat(result, `is`("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
    }

    @Test
    fun getMimeTypeForBmpExtension() {
        val result = underTest.getMimeTypeForExtension("bmp")

        assertThat(result, `is`("image/bmp"))
    }

    @Test
    fun getMimeTypeForPngExtension() {
        val result = underTest.getMimeTypeForExtension("png")

        assertThat(result, `is`("image/png"))
    }

    @Test
    fun getMimeTypeForExtension_WithDot() {
        val result = underTest.getMimeTypeForExtension(".png")

        assertThat(result, `is`("image/png"))
    }

    @Test
    fun getMimeTypeForExtension_WithStarAndDot() {
        val result = underTest.getMimeTypeForExtension("*.png")

        assertThat(result, `is`("image/png"))
    }

}