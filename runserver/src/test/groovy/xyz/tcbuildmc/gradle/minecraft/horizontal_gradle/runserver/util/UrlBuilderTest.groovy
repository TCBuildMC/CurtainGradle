package xyz.tcbuildmc.gradle.minecraft.horizontal_gradle.runserver.util

import org.apache.commons.io.FileUtils
import org.junit.jupiter.api.Test

class UrlBuilderTest {
    @Test
    void papermcTest() {
        println UrlBuilder.papermc("paper", "1.21")
    }

    @Test
    void purpurmcTest() {
        println UrlBuilder.purpur("1.21") // redirect...
    }

    @Test
    void leavesmcTest() {
        println UrlBuilder.leaves("1.20.6")
    }

    @Test
    void mohistmcTest() {
        println UrlBuilder.mohist("1.20.2")
    }

    @Test
    void testDir() {
        println FileUtils.current().absolutePath
    }
}
