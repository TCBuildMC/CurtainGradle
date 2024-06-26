package xyz.tcbuildmc.minecraft.curtaingradle.task

import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
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

    @Input
    @Optional
    Boolean extraJavaagents = false

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

            if (jarUrl.isEmpty()) {
                throw new GradleException("server jar url no provided!")
            }

            logger.lifecycle "trying downloading ${jarName} from ${jarUrl}..."
            FileUtils.copyURLToFile new URI(jarUrl).toURL(), jarFile
        }

        // 基本功能
        def cmd = ["java"]
        cmd.addAll jvmArgs

        // -javaagent
        javaagents.forEach { f ->
            cmd.add "-javaagent:${f.absolutePath}"
        }

        if (extraJavaagents) {
            def javaagentsPath = new File("${jarPath}/javaagents")

            javaagentsPath.listFiles().toList().forEach { f ->
                if (!f.isDirectory()) {
                    cmd.add "-javaagent:${f.absolutePath}"
                }
            }
        }

        if (extraClasspath) {
            def cpPath = new File("${jarPath}/classpaths")

            cpPath.listFiles().toList().forEach { f ->
                if (!f.isDirectory()) {
                    classpath.add f.absolutePath
                }
            }
        }

        if (!classpath.isEmpty()) {
            def builder = new StringBuilder()
            builder.append "--classpath "

            classpath.forEach { c ->
                builder.append c
                builder.append ";"
            }

            def s = builder.toString()
            cmd.add s.substring(0, s.length() - 1)
        }

        cmd.addAll "-jar", jarFile.absolutePath
        cmd.addAll serverArgs

        // 与工件一同运行
        if (withArtifact) {
            FileUtils.copyFileToDirectory buildTask.archiveFile.get().asFile, new File("${jarPath}/${artifactType.name().toLowerCase()}")
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
