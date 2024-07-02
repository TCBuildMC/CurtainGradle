package xyz.tcbuildmc.minecraft.curtaingradle.util

import lombok.AllArgsConstructor
import xyz.tcbuildmc.minecraft.curtaingradle.api.MapSerializable
import xyz.tcbuildmc.minecraft.curtaingradle.api.Property

@AllArgsConstructor
final class BukkitCommandContext implements MapSerializable {
    private final String description, usage
    private final List<String> aliases

    @Property("")
    @Property("permission-message")
    private final String permission, permissionMessage

    @Override
    Map<String, ?> toMap() {
        return ["description": description, "usage": usage, "aliases": aliases, "permission": permission, "permission-message": permissionMessage]
    }
}