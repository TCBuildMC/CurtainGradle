package xyz.tcbuildmc.minecraft.curtaingradle.util

import lombok.AllArgsConstructor
import xyz.tcbuildmc.minecraft.curtaingradle.api.MapSerializable

@AllArgsConstructor
final class VelocityDependency implements MapSerializable {
    private final String id
    private final boolean optional

    @Override
    Map<String, ?> toMap() {
        return ["id": id, "optional": optional]
    }
}