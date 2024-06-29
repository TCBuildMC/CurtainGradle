package xyz.tcbuildmc.minecraft.curtaingradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.GroovyCompile
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.gradle.plugins.ide.idea.model.IdeaModel
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
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
            fileName = "plugin.yml"
        }
        def bungeeCordMetadata = project.tasks.register("bungeeCordMetadata", MetadataTask) {
            fileName = "bungee.yml"
        }

        def serverRuntimeClasspath = project.configurations.maybeCreate "serverRuntimeClasspath"
        def serverRuntimeMods = project.configurations.maybeCreate "serverRuntimeMods"
        def serverRuntimePlugins = project.configurations.maybeCreate "serverRuntimePlugins"
        def runServer = project.tasks.register "runServer", RunServerTask

        def runServerWithArtifact = project.tasks.register("runServerWithArtifact", RunServerTask) {
            dependsOn project.tasks.named(JavaPlugin.JAR_TASK_NAME).get()

            withArtifact = true
        }

        setupJavaVersion project, extension.languageVersion // afterEvaluate

        if (project.plugins.hasPlugin(GroovyPlugin)) {
            setupGroovy project
        }

        if (project.plugins.hasPlugin(KotlinPluginWrapper)) {
            setupKotlin project, extension.languageVersion
        }

        if (project.plugins.hasPlugin(IdeaPlugin)) {
            setupIdea project
        }

        project.afterEvaluate {
            bukkitMetadata.configure {
                meta = extension.metadata.bukkitMetadata
            }

            bungeeCordMetadata.configure {
                meta = extension.metadata.bungeeCordMetadata
            }
        }
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

    private void setupGroovy(Project project) {
        project.tasks.withType(GroovyCompile).configureEach { t ->
            t.options.encoding = "UTF-8"
        }

        project.afterEvaluate {
            dependencies.add "compileOnly", project.dependencies.localGroovy()
        }
    }

    private void setupKotlin(Project project, int version) {
        project.tasks.withType(KotlinCompile).configureEach { t ->
            t.kotlinOptions.jvmTarget = version.toString()
        }

        project.afterEvaluate {
            dependencies.add "implementation", dependencies.create("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        }
    }

    private void setupIdea(Project project) {
        project.extensions.configure(IdeaModel) {
            module {
                downloadSources = true
                downloadJavadoc = true
            }
        }
    }
}
