package xyz.tcbuildmc.minecraft.curtaingradle

import org.gradle.api.Project
import xyz.tcbuildmc.minecraft.curtaingradle.extension.Metadata
import xyz.tcbuildmc.minecraft.curtaingradle.extension.Repositories

class CurtainGradleExtension {
    private final Project project

    CurtainGradleExtension(Project project) {
        this.project = project
    }

    int languageVersion = 8

    Repositories repositories = new Repositories(project.repositories)

    Metadata metadata = new Metadata()
}
