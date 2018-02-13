package net.dankito.mime.service.creator

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class EtcMimeTypesFileParserTest {

    private val underTest = EtcMimeTypesFileParser()


    @Test
    fun parseEtcMimeTypesFile() {
        val result = underTest.parseEtcMimeTypesFile()
        val extensions = result.values.flatten()

        assertThat(result.size, `is`(909))
        assertThat(extensions.size, `is`(1196))
    }

}