package xyz.tcbuildmc.minecraft.curtaingradle.task

import org.junit.jupiter.api.Test

class MetadataTaskTest {
    List<String> files = ["1.json", "2.toml", "3.yml", "4.h"]

    @Test
    void fileTypeTest() {
        files.forEach { s ->
            println s.substring(s.lastIndexOf(".") + 1)
        }
    }
}
