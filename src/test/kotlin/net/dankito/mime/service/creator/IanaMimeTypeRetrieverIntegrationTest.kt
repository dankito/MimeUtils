package net.dankito.mime.service.creator

import net.dankito.mime.service.FileDownloader
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class IanaMimeTypeRetrieverIntegrationTest {

    private val underTest = IanaMimeTypeRetriever(IanaMimeTypeFileParser(), FileDownloader())


    @Test
    fun retrieveAndParseAllFiles() {
        val result = underTest.retrieveAndParseAllFiles()
        val allExtensions = result.values.flatten()

        assertThat(result.size, `is`(1660))
        assertThat(allExtensions.size, `is`(1692))
    }

}