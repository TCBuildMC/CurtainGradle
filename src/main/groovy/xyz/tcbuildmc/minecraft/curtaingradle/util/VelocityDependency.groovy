package xyz.tcbuildmc.minecraft.curtaingradle.util

import xyz.tcbuildmc.minecraft.curtaingradle.api.MapSerializable

final class VelocityDependency implements MapSerializable {
    private final String id
    private final boolean optional

    VelocityDependency(String id, boolean optional) {
        this.id = id
        this.optional = optional
    }

    @Override
    Map<String, ?> toMap() {
        return ["id": id, "optional": optional]
    }
}