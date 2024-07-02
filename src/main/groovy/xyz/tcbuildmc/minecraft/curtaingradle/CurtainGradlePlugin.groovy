package xyz.tcbuildmc.minecraft.curtaingradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.scala.ScalaPlugin
import org.gradle.api.tasks.compile.GroovyCompile
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.scala.ScalaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.gradle.plugins.ide.idea.model.IdeaModel
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapperKt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import xyz.tcbuildmc.minecraft.curtaingradle.task.MetadataTask
import xyz.tcbuildmc.minecraft.curtaingradle.task.PrepareServerTask
import xyz.tcbuildmc.minecraft.curtaingradle.task.RunServerTask

class CurtainGradlePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.pluginManager.apply JavaLibraryPlugin
        project.pluginManager.apply IdeaPlugin

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
        def velocityMetadata = project.tasks.register("velocityMetadata", MetadataTask) {
            fileName = "velocity-plugin.json"
        }

        def serverRuntimeAgent = project.configurations.maybeCreate "serverRuntimeAgent"
        def serverRuntimeClasspath = project.configurations.maybeCreate "serverRuntimeClasspath"
        def serverRuntimeMods = project.configurations.maybeCreate "serverRuntimeMods"
        def serverRuntimePlugins = project.configurations.maybeCreate "serverRuntimePlugins"
        def prepareServer = project.tasks.register "prepareServer", PrepareServerTask
        def runServer = project.tasks.register("runServer", RunServerTask) {
            dependsOn prepareServer.get()
        }

        def runServerWithArtifact = project.tasks.register("runServerWithArtifact", RunServerTask) {
            dependsOn prepareServer.get(), project.tasks.named(JavaPlugin.JAR_TASK_NAME).get()

            withArtifact = true
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

        project.afterEvaluate {
            setupIdea project, prepareServer.get()

            bukkitMetadata.configure {
                meta = extension.metadata.bukkitMetadata
            }

            bungeeCordMetadata.configure {
                meta = extension.metadata.bungeeCordMetadata
            }

            velocityMetadata.configure {
                meta = extension.metadata.velocityMetadata
            }

            setupJava project, extension.lang.jdkVersion

            if (project.plugins.hasPlugin("org.jetbrains.kotlin.jvm")) {
                setupKotlin project, extension.lang.kotlinVersion, extension.lang.k2
            }

            if (project.plugins.hasPlugin(GroovyPlugin)) {
                setupGroovy project, extension.lang.groovyVersion, extension.lang.groovy4
            }

            if (project.plugins.hasPlugin(ScalaPlugin)) {
                setupScala project, extension.lang.scalaVersion
            }
        }
    }

    private void setupJava(Project project, int version) {
        project.extensions.configure(JavaPluginExtension) { e ->
            e.sourceCompatibility = version
            e.targetCompatibility = version
        }

        project.tasks.withType(JavaCompile).configureEach { t ->
            t.options.release = version
            t.options.encoding = "UTF-8"
        }
    }

    private void setupGroovy(Project project, String version, boolean groovy4) {
        project.tasks.withType(GroovyCompile).configureEach { t ->
            t.options.encoding = "UTF-8"
        }

        if (version == null) {
            project.dependencies.add "implementation", project.dependencies.localGroovy()
        } else {
            if (groovy4) {
                project.dependencies.add "implementation", project.dependencies.create(
                        "org.apache.groovy:groovy-all:${version}")
            } else {
                project.dependencies.add "implementation", project.dependencies.create(
                        "org.codehaus.groovy:groovy-all:${version}")
            }
        }
    }

    private void setupKotlin(Project project, String version, boolean k2) {
        project.tasks.withType(KotlinCompile).configureEach { t ->
            t.kotlinOptions.jvmTarget = version

            if (k2) {
                t.kotlinOptions.languageVersion = "2.0"
            }
        }

        project.dependencies.add "implementation", project.dependencies.create(
                "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${KotlinPluginWrapperKt.getKotlinPluginVersion(project)}")
    }

    private void setupScala(Project project, String version) {
        project.tasks.withType(ScalaCompile).configureEach { t ->
            t.options.encoding = "UTF-8"
        }

        project.dependencies.add "implementation", project.dependencies.create(
                "org.scala-lang:scala-library:${version}")
        project.dependencies.add "implementation", project.dependencies.create(
                "org.scala-lang:scala-compiler:${version}")
        project.dependencies.add "implementation", project.dependencies.create(
                "org.scala-lang:scala-reflect:${version}")
    }

    private void setupIdea(Project project, Task... tasks) {
        project.extensions.getByType(IdeaModel).module {
            excludeDirs.addAll(project.files(".gradle", "build", ".idea", "out").files)
            downloadJavadoc = true
            downloadSources = true
            inheritOutputDirs = true
        }
    }
}
