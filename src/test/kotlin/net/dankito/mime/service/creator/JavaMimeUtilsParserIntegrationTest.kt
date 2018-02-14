package net.dankito.mime.service.creator

import net.dankito.mime.service.FileDownloader
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Test
import java.io.File
import java.io.FileReader

class JavaMimeUtilsParserIntegrationTest {

    private val underTest = JavaMimeUtilsParser(FileDownloader())


    @Test
    fun parseJavaMimeUtilsSource() {
        val result = underTest.parseJavaMimeUtilsSource()
        val extensions = result?.values?.flatten()

        Assert.assertThat(result?.size, CoreMatchers.`is`(265))
        Assert.assertThat(extensions?.size, CoreMatchers.`is`(343))
    }

    @Test
    fun parseJavaMimeUtilsSource_ReadProvidedHtml() {
        val parseJavaMimeUtilsSourceFromHtmlMethod = JavaMimeUtilsParser::class.java.getDeclaredMethod("parseJavaMimeUtilsSource", String::class.java)
        parseJavaMimeUtilsSourceFromHtmlMethod.isAccessible = true

        val resourceUrl = JavaMimeUtilsParserIntegrationTest::class.java.classLoader.getResource(File("JavaMimeUtils_files", "JavaMimeUtilsSourceHtml_2018_02_14.html").path)

        val reader = FileReader(File(resourceUrl.toURI()))
        val javaMimeUtilsSourceHtml = reader.readText()
        reader.close()

        val result = parseJavaMimeUtilsSourceFromHtmlMethod.invoke(underTest, javaMimeUtilsSourceHtml) as? Map<String, MutableSet<String>>?
        val extensions = result?.values?.flatten()

        Assert.assertThat(result?.size, CoreMatchers.`is`(265))
        Assert.assertThat(extensions?.size, CoreMatchers.`is`(343))
    }
}