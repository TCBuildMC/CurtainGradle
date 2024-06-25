package xyz.tcbuildmc.gradle.minecraft.horizontalgradle.metadata.task

import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction

class GenerateBukkitMetadataTask extends DefaultTask {
    @Input
    Map<String, Object> meta

    @InputFile
    File resourceDir

    @TaskAction
    void execute() {
        def metadataFile = new File(resourceDir, "plugin.yml")
        def mapper = YAMLMapper.builder()
                .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                .build()

        if (!meta.containsKey("name") ||
                !meta.containsKey("version") ||
                !meta.containsKey("main") ||
                !(meta.getOrDefault("name", "") as String).isEmpty() ||
                !(meta.getOrDefault("version", "") as String).isEmpty() ||
                !(meta.getOrDefault("main", "") as String).isEmpty()) {

            throw new IllegalArgumentException()
        }

        def libraries = new ArrayList<String>()
        def librariesConfiguration = project.configurations.named("bukkitLibraries").get()

        librariesConfiguration.dependencies.forEach { d ->
            libraries.add "${d.group}:${d.name}:${d.version}"
        }

        if (meta.containsKey("libraries") && !(meta.get("libraries", new ArrayList<String>()) as List<String>).isEmpty()) {
            def lib = meta.get("libraries", new ArrayList<String>()) as List<String>
            lib.addAll libraries
        } else {
            meta["libraries"] = libraries
        }

        if (!resourceDir.exists()) {
            resourceDir.mkdirs()
        }

        if (!metadataFile.exists()) {
            metadataFile.createNewFile()
        }

        mapper.writer()
                .withDefaultPrettyPrinter()
                .writeValue(metadataFile, meta)
    }
}
