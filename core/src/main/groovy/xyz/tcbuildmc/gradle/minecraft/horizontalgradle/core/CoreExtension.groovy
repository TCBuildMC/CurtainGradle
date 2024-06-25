package xyz.tcbuildmc.gradle.minecraft.horizontalgradle.core

import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.MavenArtifactRepository

class CoreExtension {
    private final Project project

    CoreExtension(Project project) {
        this.project = project
    }

    int targetLanguageVersion = 8

    MavenArtifactRepository papermc() {
        return project.repositories.maven {
            url = "https://repo.papermc.io/repository/maven-public/"
        }
    }

    MavenArtifactRepository purpurmc() {
        return project.repositories.maven {
            url = "https://repo.purpurmc.org/snapshots/"
        }
    }

    MavenArtifactRepository leavesmc() {
        return project.repositories.maven {
            url = "https://repo.leavesmc.org/snapshots/"
        }
    }

    MavenArtifactRepository sonatype() {
        return project.repositories.maven {
            url = "https://oss.sonatype.org/content/repositories/snapshots/"
        }
    }

    MavenArtifactRepository jitpack() {
        return project.repositories.maven {
            url = "https://jitpack.io/"
        }
    }
}
