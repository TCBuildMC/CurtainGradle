package xyz.tcbuildmc.minecraft.curtaingradle.task

import xyz.tcbuildmc.minecraft.curtaingradle.CurtainGradleExtension

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
