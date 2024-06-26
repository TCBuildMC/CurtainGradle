package xyz.tcbuildmc.minecraft.curtainlib.gradle.task

import xyz.tcbuildmc.minecraft.curtainlib.gradle.CurtainGradleExtension

class BukkitMetadataTask extends AbstractMetadataTask {
    @Override
    String getFileName() {
        return "plugin.yml"
    }

    @Override
    Map<String, Object> getMeta() {
        return project.extensions.getByType(CurtainGradleExtension).metadata.bukkitMetadata
    }
}
