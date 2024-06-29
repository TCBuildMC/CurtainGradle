package xyz.tcbuildmc.minecraft.curtaingradle.task.platform

import xyz.tcbuildmc.minecraft.curtaingradle.CurtainGradleExtension
import xyz.tcbuildmc.minecraft.curtaingradle.task.MetadataTask

class BungeeCordMetadataTask extends MetadataTask {
    @Override
    String getFileName() {
        return "bungee.yml"
    }

    @Override
    Map<String, Object> getMeta() {
        return project.extensions.getByType(CurtainGradleExtension).metadata.bungeeCordMetadata
    }
}
