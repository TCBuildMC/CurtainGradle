package xyz.tcbuildmc.minecraft.curtaingradle.util

import org.junit.jupiter.api.Test

class UrlBuilderTest {
    @Test
    void paperTest() {
        println UrlBuilder.paper("1.21")
    }

    @Test
    void purpurTest() {
        println UrlBuilder.purpur("1.20.6")
    }

    @Test
    void leavesTest() {
        println UrlBuilder.leaves("1.20.6")
    }

    @Test
    void mohistTest() {
        println UrlBuilder.mohist("1.20.1")
    }
}
