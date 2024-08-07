package xyz.tcbuildmc.minecraft.curtaingradle.extension

import org.jetbrains.annotations.ApiStatus
import xyz.tcbuildmc.minecraft.curtaingradle.util.BasePermission
import xyz.tcbuildmc.minecraft.curtaingradle.util.BukkitCommandContext
import xyz.tcbuildmc.minecraft.curtaingradle.util.BukkitPermissionContext
import xyz.tcbuildmc.minecraft.curtaingradle.util.PluginLoadOrder

@Deprecated
class Metadata {
    def bukkitMetadata = new LinkedHashMap<String, Object>()
    def bungeeCordMetadata = new LinkedHashMap<String, Object>()

    def name(String name) {
        bukkitMetadata["name"] = name
        bungeeCordMetadata["name"] = name
    }

    def version(String version) {
        bukkitMetadata["version"] = version
        bungeeCordMetadata["version"] = version
    }

    def main(String main) {
        bukkitMetadata["main"] = main
        bungeeCordMetadata["main"] = main
    }

    def description(String description) {
        bukkitMetadata["description"] = description
        bungeeCordMetadata["description"] = description
    }

    def author(String author) {
        bukkitMetadata["author"] = author
        bungeeCordMetadata["author"] = author
    }

    def authors(List<String> authors) {
        bukkitMetadata["authors"] = authors
    }

    def contributors(List<String> contributors) {
        bukkitMetadata["contributors"] = contributors
    }

    def website(String website) {
        bukkitMetadata["website"] = website
    }

    def apiVersion(String apiVersion) {
        bukkitMetadata["api-version"] = apiVersion
    }

    def load(PluginLoadOrder order) {
        bukkitMetadata["load"] = order.name()
    }

    def load(String order) {
        load(PluginLoadOrder.valueOf(order))
    }

    def prefix(String prefix) {
        bukkitMetadata["prefix"] = prefix
    }

    @ApiStatus.Experimental
    def libraries(List<String> libraries) {
        bukkitMetadata["libraries"] = libraries
        bungeeCordMetadata["libraries"] = libraries
    }

    @ApiStatus.Experimental
    def permissions(Map<String, BukkitPermissionContext> permissionContextMap) {
        def map = new LinkedHashMap<String, Map<String, Object>>()

        permissionContextMap.forEach { s, c ->
            map.put s, c.toMap()
        }

        bukkitMetadata["permissions"] = map
    }

    def defaultPermission(BasePermission permission) {
        bukkitMetadata["default-permission"] = permission.toString()
    }

    def defaultPermission(String permission) {
        bukkitMetadata["default-permission"] = BasePermission.valueOf(permission).toString()
    }

    @ApiStatus.Experimental
    def commands(Map<String, BukkitCommandContext> commandContextMap) {
        def map = new LinkedHashMap<String, Map<String, Object>>()

        commandContextMap.forEach { s, c ->
            map.put s, c.toMap()
        }

        bukkitMetadata["commands"] = map
    }

    def depend(List<String> depend) {
        bukkitMetadata["depend"] = depend
        bungeeCordMetadata["depends"] = depend
    }

    def softdepend(List<String> softdepend) {
        bukkitMetadata["softdepend"] = softdepend
        bungeeCordMetadata["softDepends"] = softdepend
    }

    def loadbefore(List<String> loadbefore) {
        bukkitMetadata["loadbefore"] = loadbefore
    }

    @ApiStatus.Experimental
    def provides(List<String> provides) {
        bukkitMetadata["provides"] = provides
    }
}
