package xyz.tcbuildmc.minecraft.curtaingradle.util

import xyz.tcbuildmc.minecraft.curtaingradle.api.MapSerializable
import xyz.tcbuildmc.minecraft.curtaingradle.api.Property

final class BukkitPermissionContext implements MapSerializable {
    private final String description

    @Property("default")
    private final BasePermission defaultPermission
    private final Map<String, Boolean> children

    BukkitPermissionContext(String description, BasePermission defaultPermission, Map<String, Boolean> children) {
        this.description = description
        this.defaultPermission = defaultPermission
        this.children = children
    }

    @Override
    Map<String, ?> toMap() {
        return ["description": description, "default": defaultPermission.toString(), "children": children]
    }
}