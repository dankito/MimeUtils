package net.dankito.mime

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.number.OrderingComparison
import org.junit.Assert.assertThat
import org.junit.Test

class MimeTypePickerTest {

    private val detector = MimeTypeDetector()

    private val underTest = MimeTypePicker()


    @Test
    fun getBestPickForJpeg() {
        getAndTestBestPick("jpeg", "image/jpeg")
    }

    @Test
    fun getBestPickForTiff() {
        getAndTestBestPick("tiff", "image/tiff")
    }

    @Test
    fun getBestPickForBmp() {
        getAndTestBestPick("bmp", "image/bmp")
    }

    @Test
    fun getBestPickForMp3() {
        getAndTestBestPick("mp3", "audio/mpeg")
    }

    @Test
    fun getBestPickForAvi() {
        getAndTestBestPick("avi", "video/avi")
    }

    @Test
    fun getBestPickForMpeg() {
        getAndTestBestPick("mpeg", "video/mpeg")
    }

    @Test
    fun getBestPickForOgg() {
        getAndTestBestPick("ogg", "audio/ogg")
    }

    @Test
    fun getBestPickFor3gpp() {
        getAndTestBestPick("3gpp", "video/3gpp")
    }

    @Test
    fun getBestPickForText() {
        getAndTestBestPick("text", "text/plain")
    }

    @Test
    fun getBestPickForC() {
        getAndTestBestPick("c", "text/plain")
    }

    @Test
    fun getBestPickForXlt() {
        getAndTestBestPick("xlt", "application/excel")
    }

    @Test
    fun getBestPickForGzip() {
        getAndTestBestPick("gzip", "application/gzip")
    }


    private fun getAndTestBestPick(fileExtension: String, bestMimeTypePick: String) {
        val allExtensionMimeTypes = detector.getMimeTypesForExtension(fileExtension)

        assertThat(allExtensionMimeTypes, notNullValue())
        assertThat(allExtensionMimeTypes?.size ?: 0, OrderingComparison.greaterThan(1))


        val bestPick = underTest.getBestPick(allExtensionMimeTypes)
        assertThat(bestPick, `is`(bestMimeTypePick))
    }

}