package xyz.tcbuildmc.minecraft.curtaingradle.extension

import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository

class Repositories {
    private final RepositoryHandler repositories

    Repositories(RepositoryHandler repositories) {
        this.repositories = repositories
    }

    MavenArtifactRepository papermc() {
        return repositories.maven {
            url = "https://repo.papermc.io/repository/maven-public/"
        }
    }

    MavenArtifactRepository purpurmc() {
        return repositories.maven {
            url = "https://repo.purpurmc.org/snapshots/"
        }
    }

    MavenArtifactRepository leavesmc() {
        return repositories.maven {
            url = "https://repo.leavesmc.org/snapshots/"
        }
    }

    MavenArtifactRepository sonatype() {
        return repositories.maven {
            url = "https://oss.sonatype.org/content/repositories/snapshots/"
        }
    }

    MavenArtifactRepository jitpack() {
        return repositories.maven {
            url = "https://jitpack.io/"
        }
    }
}
