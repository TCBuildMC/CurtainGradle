package xyz.tcbuildmc.minecraft.curtaingradle.task.platform

import xyz.tcbuildmc.minecraft.curtaingradle.CurtainGradleExtension
import xyz.tcbuildmc.minecraft.curtaingradle.task.MetadataTask

class BukkitMetadataTask extends MetadataTask {
    @Override
    String getFileName() {
        return "plugin.yml"
    }

    @Override
    Map<String, Object> getMeta() {
        return project.extensions.getByType(CurtainGradleExtension).metadata.bukkitMetadata
    }
}
