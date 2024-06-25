package xyz.tcbuildmc.gradle.minecraft.horizontalgradle.runserver

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import xyz.tcbuildmc.gradle.minecraft.horizontalgradle.runserver.task.RunServerTask

class RunServerPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        def serverRuntimeClasspath = project.configurations.maybeCreate "serverRuntimeClasspath"
        def serverRuntimeMods = project.configurations.maybeCreate "serverRuntimeMods"
        def serverRuntimePlugins = project.configurations.maybeCreate "serverRuntimePlugins"

        def runServer = project.tasks.register("runServer", RunServerTask) {
            jarPath = new File("${project.rootDir}/run")
            jarName = "server.jar"
        }

        def runServerWithArtifact = project.tasks.register("runServerWithArtifact", RunServerTask) {
            dependsOn project.tasks.named(JavaPlugin.JAR_TASK_NAME).get()
            jarPath = new File("${project.rootDir}/run")
            jarName = "server.jar"

            runWithArtifact = true
        }
    }
}
