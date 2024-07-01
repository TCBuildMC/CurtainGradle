package xyz.tcbuildmc.minecraft.curtaingradle.task

import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

class PrepareServerTask extends DefaultTask {
    @InputFile
    @Optional
    File jarPath = new File("${project.projectDir}/run")

    @Input
    @Optional
    List<String> javaagentsUrls = new ArrayList<>()

    @Input
    @Optional
    List<Configuration> javaagentsConfigurations = [project.configurations.named("serverRuntimeAgent").get()]

    @Input
    @Optional
    List<String> runtimeClasspathUrls = new ArrayList<>()

    @Input
    @Optional
    List<Configuration> runtimeClasspathConfigurations = [project.configurations.named("serverRuntimeClasspath").get()]

    @InputFiles
    @Optional
    List<File> runtimeMods = new ArrayList<>()

    @InputFiles
    @Optional
    List<File> runtimePlugins = new ArrayList<>()

    @Input
    @Optional
    List<String> runtimeModsUrls = new ArrayList<>()

    @Input
    @Optional
    List<String> runtimePluginsUrls = new ArrayList<>()

    @Input
    @Optional
    List<Configuration> runtimeModsConfigurations = [project.configurations.named("serverRuntimeMods").get()]

    @Input
    @Optional
    List<Configuration> runtimePluginsConfigurations = [project.configurations.named("serverRuntimePlugins").get()]

    @TaskAction
    void execute() {
        def javaagentsPath = new File("${jarPath}/javaagents")

        javaagentsUrls.forEach { s ->
            def destFile = new File(javaagentsPath, getFileNameFromURL(s))

            FileUtils.copyURLToFile new URI(s).toURL(), destFile
        }

        javaagentsConfigurations.forEach { c ->
            c.resolve().forEach { f ->
                FileUtils.copyFileToDirectory f, javaagentsPath
            }
        }

        def cpPath = new File("${jarPath}/classpaths")

        runtimeClasspathUrls.forEach { s ->
            def destFile = new File(cpPath, getFileNameFromURL(s))

            FileUtils.copyURLToFile new URI(s).toURL(), destFile
        }

        runtimeClasspathConfigurations.forEach { c ->
            c.resolve().forEach { f ->
                FileUtils.copyFileToDirectory f, cpPath
            }
        }

        def modsPath = new File("${jarPath}/mods")
        def pluginsPath = new File("${jarPath}/plugins")

        runtimeMods.forEach { f ->
            FileUtils.copyFileToDirectory f, modsPath
        }

        runtimePlugins.forEach { f ->
            FileUtils.copyFileToDirectory f, pluginsPath
        }

        runtimeModsUrls.forEach { s ->
            FileUtils.copyURLToFile(new URI(s).toURL(), new File(modsPath, getFileNameFromURL(s)))
        }

        runtimePluginsUrls.forEach { s ->
            FileUtils.copyURLToFile(new URI(s).toURL(), new File(pluginsPath, getFileNameFromURL(s)))
        }

        runtimeModsConfigurations.forEach { c ->
            c.resolve().forEach { f ->
                FileUtils.copyFileToDirectory f, modsPath
            }
        }

        runtimePluginsConfigurations.forEach { c ->
            c.resolve().forEach { f ->
                FileUtils.copyFileToDirectory f, pluginsPath
            }
        }
    }

    private String getFileNameFromURL(String url) {
        return url.substring(url.lastIndexOf("/") + 1)
    }
}
