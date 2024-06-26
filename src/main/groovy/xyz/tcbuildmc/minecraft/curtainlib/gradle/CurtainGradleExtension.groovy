package xyz.tcbuildmc.minecraft.curtainlib.gradle

import org.gradle.api.Project
import xyz.tcbuildmc.minecraft.curtainlib.gradle.extension.Metadata
import xyz.tcbuildmc.minecraft.curtainlib.gradle.extension.Repositories

class CurtainGradleExtension {
    private final Project project

    CurtainGradleExtension(Project project) {
        this.project = project
    }

    int languageVersion = 8

    Repositories repositories = new Repositories(project.repositories)

    Metadata metadata = new Metadata()
}
