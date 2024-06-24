package xyz.tcbuildmc.gradle.minecraft.horizontal_gradle.core

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.toolchain.JavaLanguageVersion

class CorePlugin implements Plugin<Project> {
    @Override
    void apply(Project p) {
        p.pluginManager.apply(JavaLibraryPlugin)

        def ext = p.extensions.create("horizontalGradle", CoreExtension, p)

        setupJava(p, ext.targetLanguageVersion)
    }

    private void setupJava(Project p, int target) {
        p.extensions.configure(JavaPluginExtension) {
            sourceCompatibility = target
            targetCompatibility = target

            toolchain {
                languageVersion.set(JavaLanguageVersion.of(target))
            }
        }

        p.tasks.withType(JavaCompile).configureEach {
            options.release.set(target)
            options.encoding = "UTF-8"
        }

        p.tasks.withType(Test).configureEach {
            systemProperty "file.encoding", "UTF-8"

            ignoreFailures = true

            testLogging {
                exceptionFormat = "full"
                events "passed", "skipped", "failed"
            }
        }

        p.tasks.named(JavaPlugin.COMPILE_JAVA_TASK_NAME).get().dependsOn(p.tasks.named(BasePlugin.CLEAN_TASK_NAME))
    }
}
