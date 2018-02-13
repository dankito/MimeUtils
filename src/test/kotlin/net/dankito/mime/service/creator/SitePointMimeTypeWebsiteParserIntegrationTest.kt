package net.dankito.mime.service.creator

import net.dankito.mime.service.FileDownloader
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import java.io.File
import java.io.FileReader

class SitePointMimeTypeWebsiteParserIntegrationTest {

    private val underTest = SitePointMimeTypeWebsiteParser(FileDownloader())


    @Test
    fun parseSitePointWebsite() {
        val result = underTest.parseSitePointWebsite()
        val extensions = result?.values?.flatten()

        assertThat(result?.size, `is`(416))
        assertThat(extensions?.size, `is`(646))
    }

    @Test
    fun parseSitePointWebsite_ReadProvidedHtml() {
        val parseSitePointWebsiteFromHtmlMethod = SitePointMimeTypeWebsiteParser::class.java.getDeclaredMethod("parseSitePointWebsite", String::class.java)
        parseSitePointWebsiteFromHtmlMethod.isAccessible = true

        val resourceUrl = SitePointMimeTypeWebsiteParserIntegrationTest::class.java.classLoader.getResource(File("SitePoint_files", "SitePointMimeTypesCompleteList_2018_02_13.html").path)

        val reader = FileReader(File(resourceUrl.toURI()))
        val sitePointWebsiteHtml = reader.readText()
        reader.close()

        val result = parseSitePointWebsiteFromHtmlMethod.invoke(underTest, sitePointWebsiteHtml) as? Map<String, MutableSet<String>>?
        val extensions = result?.values?.flatten()

        assertThat(result?.size, `is`(416))
        assertThat(extensions?.size, `is`(646))
    }

}