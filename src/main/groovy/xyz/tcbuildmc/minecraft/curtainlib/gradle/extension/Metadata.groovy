package xyz.tcbuildmc.minecraft.curtainlib.gradle.extension

import org.jetbrains.annotations.ApiStatus
import xyz.tcbuildmc.minecraft.curtainlib.gradle.util.BasePermission
import xyz.tcbuildmc.minecraft.curtainlib.gradle.util.PluginLoadOrder

class Metadata {
    def bukkitMetadata = new LinkedHashMap<String, Object>()

    def name(String name) {
        bukkitMetadata["name"] = name
    }

    def version(String version) {
        bukkitMetadata["version"] = version
    }

    def main(String main) {
        bukkitMetadata["main"] = main
    }

    def description(String description) {
        bukkitMetadata["description"] = description
    }

    def author(String author) {
        bukkitMetadata["author"] = author
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
    }

    @ApiStatus.Experimental
    def permissions() {
    }

    def defaultPermission(BasePermission permission) {
        bukkitMetadata["default-permission"] = permission.id
    }

    def defaultPermission(String permission) {
        bukkitMetadata["default-permission"] = BasePermission.valueOf(permission).id
    }

    @ApiStatus.Experimental
    def commands() {
    }

    def depend(List<String> depend) {
        bukkitMetadata["depend"] = depend
    }

    def softdepend(List<String> softdepend) {
        bukkitMetadata["softdepend"] = softdepend
    }

    def loadbefore(List<String> loadbefore) {
        bukkitMetadata["loadbefore"] = loadbefore
    }

    @ApiStatus.Experimental
    def provides(List<String> provides) {
        bukkitMetadata["provides"] = provides
    }
}
