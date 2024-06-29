package xyz.tcbuildmc.minecraft.curtaingradle.task

import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.artifacts.Configuration
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.bundling.AbstractArchiveTask
import xyz.tcbuildmc.minecraft.curtaingradle.util.ArtifactType

class RunServerTask extends DefaultTask {
    // 基本功能
    @InputFile
    @Optional
    File jarPath = new File("${project.projectDir}/run")

    @Input
    @Optional
    String jarName = "server.jar"

    @Input
    @Optional
    List<String> jvmArgs = new ArrayList<>()

    @Input
    @Optional
    List<String> classpath = new ArrayList<>()

    @InputFiles
    @Optional
    List<File> javaagents = new ArrayList<>()

    @Input
    @Optional
    List<String> serverArgs = new ArrayList<>()

    // 下载
    @Input
    @Optional
    String jarUrl = ""

    // 与工件一同运行
    @Input
    @Optional
    Boolean withArtifact = false

    @Input
    @Optional
    AbstractArchiveTask buildTask = project.tasks.named(JavaPlugin.JAR_TASK_NAME, AbstractArchiveTask).get()

    @Input
    @Optional
    ArtifactType artifactType = ArtifactType.PLUGINS

    // 与运行时依赖一同运行
    @Input
    @Optional
    Boolean extraClasspath = false

    @InputFiles
    @Optional
    List<File> runtimeClasspath = new ArrayList<>()

    @Input
    @Optional
    List<String> runtimeClasspathUrls = new ArrayList<>()

    @Input
    @Optional
    List<Configuration> runtimeClasspathConfigurations = [project.configurations.named("serverRuntimeClasspath").get()]

    // 运行时 mod 或 插件
    @Input
    @Optional
    Boolean extraModsAndPlugins = false

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

    @Input
    @Optional
    Boolean eula = false

    RunServerTask() {
        this.group = "curtainGradle"
        this.description = "Launch Minecraft Server for development environment."
    }

    @TaskAction
    void execute() {
        if (!jarPath.exists()) {
            jarPath.mkdirs()
        }

        def jarFile = new File(jarPath, jarName)

        if (!jarFile.exists()) {
            logger.warn "server jar don't exist"

            if (jarUrl.length() == 0) {
                throw new GradleException("server jar url no provided!")
            }

            logger.lifecycle "trying downloading ${jarName} from ${jarUrl}..."
            FileUtils.copyURLToFile(new URI(jarUrl).toURL(), jarFile)
        }

        // 基本功能
        def cmd = ["java"]
        cmd.addAll jvmArgs

        // -javaagent
        javaagents.forEach { f ->
            cmd.add "-javaagent:${f.absolutePath}"
        }

        // -cp
        runtimeClasspath.forEach { f ->
            classpath.add f.absolutePath
        }

        if (extraClasspath) {
            runtimeClasspathUrls.forEach { s ->
                def fileName = s.substring(s.lastIndexOf("/") + 1)
                def destFile = new File("${jarPath}/cp", fileName)

                FileUtils.copyURLToFile(new URI(s).toURL(), destFile)
                classpath.add destFile.absolutePath
            }

            runtimeClasspathConfigurations.forEach { c ->
                c.resolve().forEach { f ->
                    classpath.add f.absolutePath
                }
            }
        }

        if (!classpath.isEmpty()) {
            cmd.add "--classpath"
            cmd.addAll classpath
        }

        cmd.addAll "-jar", jarFile.absolutePath
        cmd.addAll serverArgs

        // 与工件一同运行
        if (withArtifact) {
            FileUtils.copyFileToDirectory buildTask.archiveFile.get().asFile, new File("${jarPath}/${artifactType.name().toLowerCase()}")
        }

        // 与运行时依赖一同运行
        if (extraModsAndPlugins) {
            runtimeMods.forEach { f ->
                FileUtils.copyFileToDirectory f, new File("${jarPath}/mods")
            }

            runtimePlugins.forEach { f ->
                FileUtils.copyFileToDirectory f, new File("${jarPath}/plugins")
            }

            runtimeModsUrls.forEach { s ->
                def fileName = s.substring(s.lastIndexOf("/") + 1)
                FileUtils.copyURLToFile(new URI(s).toURL(), new File("${jarPath}/mods", fileName))
            }

            runtimePluginsUrls.forEach { s ->
                def fileName = s.substring(s.lastIndexOf("/") + 1)
                FileUtils.copyURLToFile(new URI(s).toURL(), new File("${jarPath}/plugins", fileName))
            }

            runtimeModsConfigurations.forEach { c ->
                c.resolve().forEach { f ->
                    FileUtils.copyFileToDirectory f, new File("${jarPath}/mods")
                }
            }

            runtimePluginsConfigurations.forEach { c ->
                c.resolve().forEach { f ->
                    FileUtils.copyFileToDirectory f, new File("${jarPath}/mods")
                }
            }
        }

        if (eula) {
            def eulaFile = new File(jarPath, "eula.txt")
            if (!eulaFile.exists() || !eulaFile.readLines().contains("eula=true")) {
                eulaFile.write("eula=true")
            }
        }

        project.exec {
            commandLine = cmd
            workingDir = jarPath
            standardOutput = System.out
        }
    }
}
