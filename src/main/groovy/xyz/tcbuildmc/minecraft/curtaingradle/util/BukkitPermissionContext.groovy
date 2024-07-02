package xyz.tcbuildmc.minecraft.curtaingradle.util

import lombok.AllArgsConstructor
import xyz.tcbuildmc.minecraft.curtaingradle.api.MapSerializable
import xyz.tcbuildmc.minecraft.curtaingradle.api.Property

@AllArgsConstructor
final class BukkitPermissionContext implements MapSerializable {
    private final String description

    @Property("default")
    private final BasePermission defaultPermission
    private final Map<String, Boolean> children

    @Override
    Map<String, ?> toMap() {
        return ["description": description, "default": defaultPermission.toString(), "children": children]
    }
}