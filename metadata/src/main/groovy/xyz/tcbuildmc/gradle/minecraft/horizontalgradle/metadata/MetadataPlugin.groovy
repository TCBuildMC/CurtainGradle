package xyz.tcbuildmc.gradle.minecraft.horizontalgradle.metadata

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import xyz.tcbuildmc.gradle.minecraft.horizontalgradle.metadata.task.GenerateBukkitMetadataTask

class MetadataPlugin implements Plugin<Project> {
    @Override
    void apply(Project p) {
        def bukkitLibraries = p.configurations.maybeCreate "bukkitLibraries"

        def ext = p.extensions.create("horizontalGradleMetadata", MetadataExtension)

        def bukkitMetadata = p.tasks.register("bukkitMetadata", GenerateBukkitMetadataTask) {
            dependsOn project.tasks.named(JavaPlugin.PROCESS_RESOURCES_TASK_NAME).get()

            meta = ext.bukkitMetadata
            resourceDir = p.projectDir.toPath().resolve("src").resolve(ext.sourceSet).resolve("resources").toFile()
        }
    }
}
