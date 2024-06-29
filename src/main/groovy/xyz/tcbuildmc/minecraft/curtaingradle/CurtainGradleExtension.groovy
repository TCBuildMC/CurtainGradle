package xyz.tcbuildmc.minecraft.curtaingradle

import org.gradle.api.Action
import org.gradle.api.Project
import xyz.tcbuildmc.minecraft.curtaingradle.extension.Dependencies
import xyz.tcbuildmc.minecraft.curtaingradle.extension.Lang
import xyz.tcbuildmc.minecraft.curtaingradle.extension.Metadata
import xyz.tcbuildmc.minecraft.curtaingradle.extension.Repositories

class CurtainGradleExtension {
    private final Project project

    CurtainGradleExtension(Project project) {
        this.project = project
    }

    Lang lang = new Lang()

    Repositories repositories = new Repositories(project.repositories)

    Metadata metadata = new Metadata()

    Dependencies dependencies = new Dependencies()

    def lang(Action<? super Lang> action) {
        action.execute lang
    }

    def repositories(Action<? super Repositories> action) {
        action.execute repositories
    }

    def metadata(Action<? super Metadata> action) {
        action.execute metadata
    }

    def dependencies(Action<? super Dependencies> action) {
        action.execute dependencies
    }
}
