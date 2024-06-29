package xyz.tcbuildmc.minecraft.curtaingradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import xyz.tcbuildmc.minecraft.curtaingradle.task.MetadataTask
import xyz.tcbuildmc.minecraft.curtaingradle.task.RunServerTask

class CurtainGradlePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.pluginManager.apply JavaLibraryPlugin

        project.logger.lifecycle "CurtainGradle: ${CurtainGradlePlugin.class.package.implementationVersion}"
        project.logger.lifecycle "by TCBuildMC"
        def extension = project.extensions.create "curtainGradle", CurtainGradleExtension, project

        def bukkitLibrary = project.configurations.maybeCreate "bukkitLibrary"
        def bukkitMetadata = project.tasks.register("bukkitMetadata", MetadataTask) {
            dependsOn project.tasks.named(JavaPlugin.PROCESS_RESOURCES_TASK_NAME).get()

            fileName = "plugin.yml"
            meta = extension.metadata.bukkitMetadata
        }
        project.tasks.named(JavaPlugin.PROCESS_RESOURCES_TASK_NAME).get().dependsOn bukkitMetadata.get()

        def bungeeCordMetadata = project.tasks.register("bungeeCordMetadata", MetadataTask) {
            dependsOn project.tasks.named(JavaPlugin.PROCESS_RESOURCES_TASK_NAME).get()

            fileName = "bungee.yml"
            meta = extension.metadata.bungeeCordMetadata
        }
        project.tasks.named(JavaPlugin.PROCESS_RESOURCES_TASK_NAME).get().dependsOn bungeeCordMetadata.get()

        def serverRuntimeClasspath = project.configurations.maybeCreate "serverRuntimeClasspath"
        def serverRuntimeMods = project.configurations.maybeCreate "serverRuntimeMods"
        def serverRuntimePlugins = project.configurations.maybeCreate "serverRuntimePlugins"
        def runServer = project.tasks.register "runServer", RunServerTask

        def runServerWithArtifact = project.tasks.register("runServerWithArtifact", RunServerTask) {
            dependsOn project.tasks.named(JavaPlugin.JAR_TASK_NAME).get()

            withArtifact = true
        }

        setupJavaVersion project, extension.languageVersion
    }

    private void setupJavaVersion(Project project, int version) {
        project.extensions.configure(JavaPluginExtension) { e ->
            e.sourceCompatibility = version
            e.targetCompatibility = version
        }

        project.tasks.withType(JavaCompile).configureEach { t ->
            t.options.release.set version
            t.options.encoding = "UTF-8"
        }

        project.tasks.withType(Test).configureEach { t ->
            t.systemProperty "file.encoding", "UTF-8"

            t.ignoreFailures = true

            t.testLogging {
                exceptionFormat = "full"
                events "passed", "skipped", "failed"
            }
        }

        project.tasks.named(JavaPlugin.COMPILE_JAVA_TASK_NAME).get().dependsOn(project.tasks.named(BasePlugin.CLEAN_TASK_NAME))
    }
}