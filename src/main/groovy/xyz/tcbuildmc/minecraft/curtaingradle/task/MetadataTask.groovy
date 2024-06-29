package xyz.tcbuildmc.minecraft.curtaingradle.task

import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

abstract class MetadataTask extends DefaultTask {
    @Input
    abstract String getFileName()

    @Input
    abstract Map<String, Object> getMeta()

    @Input
    @Optional
    String sourceSet = "main"

    MetadataTask() {
        this.group = "curtainGradle"
        this.description = "Generate Metadata file when building project."
    }

    @TaskAction
    final void execute() {
        def metadataFile = new File(project.projectDir
                .toPath()
                .resolve("src")
                .resolve(sourceSet)
                .resolve("resources")
                .toFile(), fileName)

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

        if (!metadataFile.parentFile.exists()) {
            metadataFile.parentFile.mkdirs()
        }

        if (!metadataFile.exists()) {
            metadataFile.createNewFile()
        }

        mapper.writer()
                .withDefaultPrettyPrinter()
                .writeValue(metadataFile, meta)
    }
}
