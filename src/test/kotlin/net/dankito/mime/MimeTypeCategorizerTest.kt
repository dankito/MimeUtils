package net.dankito.mime

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class MimeTypeCategorizerTest {

    private val underTest = MimeTypeCategorizer()


    @Test
    fun png_isImageFile() {
        assertThat(underTest.isImageFile("image/png"), `is`(true))
    }

    @Test
    fun pngUpperCase_isImageFile() {
        assertThat(underTest.isImageFile("IMAGE/png"), `is`(true))
    }

    @Test
    fun pngMixedCase_isImageFile() {
        assertThat(underTest.isImageFile("iMage/pNg"), `is`(true))
    }

    @Test
    fun jpg_isImageFile() {
        assertThat(underTest.isImageFile("image/jpeg"), `is`(true))
    }

    @Test
    fun jpgUpperCase_isImageFile() {
        assertThat(underTest.isImageFile("IMAGE/JPEG"), `is`(true))
    }

    @Test
    fun jpgMixedCase_isImageFile() {
        assertThat(underTest.isImageFile("Image/JpeP"), `is`(true))
    }

    @Test
    fun gif_isImageFile() {
        assertThat(underTest.isImageFile("image/gif"), `is`(true))
    }

    @Test
    fun gifUpperCase_isImageFile() {
        assertThat(underTest.isImageFile("IMAGE/GIF"), `is`(true))
    }

    @Test
    fun gifMixedCase_isImageFile() {
        assertThat(underTest.isImageFile("ImaGe/giF"), `is`(true))
    }

    @Test
    fun bmp_isImageFile() {
        assertThat(underTest.isImageFile("image/bmp"), `is`(true))
    }

    @Test
    fun bmpUpperCase_isImageFile() {
        assertThat(underTest.isImageFile("IMAGE/BMP"), `is`(true))
    }

    @Test
    fun bmpMixedCase_isImageFile() {
        assertThat(underTest.isImageFile("ImAge/bMp"), `is`(true))
    }


    @Test
    fun pdf_isNotImageFile() {
        assertThat(underTest.isImageFile("application/pdf"), `is`(false))
    }

    @Test
    fun docx_isNotImageFile() {
        assertThat(underTest.isImageFile("application/vnd.openxmlformats-officedocument.wordprocessingml.document"), `is`(false))
    }

    @Test
    fun html_isNotImageFile() {
        assertThat(underTest.isImageFile("text/html"), `is`(false))
    }


    @Test
    fun isAudioFile() {
    }

    @Test
    fun isVideoFile() {
    }


    @Test
    fun txt_isTextFile() {
        assertThat(underTest.isTextFile("text/plain"), `is`(true))
    }

    @Test
    fun docx_isNotTextFile() {
        assertThat(underTest.isTextFile("application/vnd.openxmlformats-officedocument.wordprocessingml.document"), `is`(false))
    }


    @Test
    fun txt_isDocument() {
        assertThat(underTest.isDocument("text/plain"), `is`(true))
    }

    @Test
    fun docx_isDocument() {
        assertThat(underTest.isDocument("application/vnd.openxmlformats-officedocument.wordprocessingml.document"), `is`(true))
    }

    @Test
    fun dotm_isDocument() {
        assertThat(underTest.isDocument("application/vnd.ms-word.template.macroEnabled.12"), `is`(true))
    }

    @Test
    fun xlsx_isDocument() {
        assertThat(underTest.isDocument("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"), `is`(true))
    }

    @Test
    fun xls_isDocument() {
        assertThat(underTest.isDocument("application/vnd.ms-excel"), `is`(true))
    }

    @Test
    fun xlsb_isDocument() {
        assertThat(underTest.isDocument("application/vnd.ms-excel.sheet.binary.macroenabled.12"), `is`(true))
    }

    @Test
    fun xltm_isDocument() {
        assertThat(underTest.isDocument("application/vnd.ms-excel.template.macroenabled.12"), `is`(true))
    }

    @Test
    fun pptx_isDocument() {
        assertThat(underTest.isDocument("application/vnd.openxmlformats-officedocument.presentationml.presentation"), `is`(true))
    }

    @Test
    fun ppt_isDocument() {
        assertThat(underTest.isDocument("application/vnd.ms-powerpoint"), `is`(true))
    }

    @Test
    fun ppsx_isDocument() {
        assertThat(underTest.isDocument("application/vnd.openxmlformats-officedocument.presentationml.slideshow"), `is`(true))
    }

    @Test
    fun sldx_isDocument() {
        assertThat(underTest.isDocument("application/vnd.openxmlformats-officedocument.presentationml.slide"), `is`(true))
    }

    @Test
    fun ppsm_isDocument() {
        assertThat(underTest.isDocument("application/vnd.ms-powerpoint.slideshow.macroenabled.12"), `is`(true))
    }


    @Test
    fun pdf_isPdfFile() {
        assertThat(underTest.isPdfFile("application/pdf"), `is`(true))
    }

    @Test
    fun docx_isNotPdfFile() {
        assertThat(underTest.isPdfFile("application/vnd.openxmlformats-officedocument.wordprocessingml.document"), `is`(false))
    }
}