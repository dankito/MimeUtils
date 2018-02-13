package net.dankito.mime.service.creator

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import java.io.File


class IanaMimeTypeFileParserTest {

    private val underTest = IanaMimeTypeFileParser()


    @Test
    fun parseIanaApplicationCsvFile() {
        // application.csv has 1273 lines:
        // one is the header, two are duplicates (activemessage and vnd.macports.portpkg) and one is temporary and unparsable ("mud+json (TEMPORARY - registered 2016-11-17, extension registered 2017-10-02, expires 2018-11-17)",application/mud+json,[draft-ietf-opsawg-mud])
        parseAndTestFile("application.csv", 1269)
    }

    @Test
    fun parseIanaAudioCsvFile() {
        // audio.csv has 149 lines, one is the header
        parseAndTestFile("audio.csv", 148)
    }

    @Test
    fun parseIanaFontCsvFile() {
        // font.csv has 7 lines, one is the header
        parseAndTestFile("font.csv", 6)
    }

    @Test
    fun parseIanaImageCsvFile() {
        // image.csv has 58 lines, one is the header
        parseAndTestFile("image.csv", 57)
    }

    @Test
    fun parseIanaMessageCsvFile() {
        // message.csv has 22 lines, one is the header
        parseAndTestFile("message.csv", 21)
    }

    @Test
    fun parseIanaModelCsvFile() {
        // model.csv has 25 lines, one is the header
        parseAndTestFile("model.csv", 24)
    }

    @Test
    fun parseIanaMultipartCsvFile() {
        // multipart.csv has 18 lines, one is the header
        parseAndTestFile("multipart.csv", 17)
    }

    @Test
    fun parseIanaTextCsvFile() {
        // text.csv has 74 lines
        parseAndTestFile("text.csv", 73)
    }

    @Test
    fun parseIanaVideoCsvFile() {
        // video.csv has 81 lines:
        parseAndTestFile("video.csv", 80)
    }


    private fun parseAndTestFile(fileResourceName: String, countExtensionsInFile: Int) {
        val resourceUrl = IanaMimeTypeFileParserTest::class.java.classLoader.getResource(File("IANA_files", fileResourceName).path)

        val result = underTest.parseIanaCsvFile(File(resourceUrl.toURI()))

        val extensions = result.values.flatten()
        assertThat(extensions.size, `is`(countExtensionsInFile))
    }

}