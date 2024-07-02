package xyz.tcbuildmc.minecraft.curtaingradle.util

import xyz.tcbuildmc.minecraft.curtaingradle.api.MapSerializable
import xyz.tcbuildmc.minecraft.curtaingradle.api.Property

final class BukkitCommandContext implements MapSerializable {
    private final String description, usage
    private final List<String> aliases

    @Property("")
    @Property("permission-message")
    private final String permission, permissionMessage

    BukkitCommandContext(String description, String usage, List<String> aliases, String permission, String permissionMessage) {
        this.description = description
        this.usage = usage
        this.aliases = aliases
        this.permission = permission
        this.permissionMessage = permissionMessage
    }

    @Override
    Map<String, ?> toMap() {
        return ["description": description, "usage": usage, "aliases": aliases, "permission": permission, "permission-message": permissionMessage]
    }
}