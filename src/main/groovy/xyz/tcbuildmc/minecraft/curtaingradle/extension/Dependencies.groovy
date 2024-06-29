package xyz.tcbuildmc.minecraft.curtaingradle.extension

class Dependencies {
    String bukkitApi(String version) {
        return "org.bukkit:bukkit:${version}"
    }

    String spigotApi(String version) {
        return "org.spigotmc:spigot-api:${version}"
    }

    String paperApi(String version) {
        return "io.papermc:paper-api:${version}"
    }

    String purpurApi(String version) {
        return "org.purpurmc.purpur:purpur-api:${version}"
    }

    String leavesApi(String version) {
        return "org.leavesmc.leaves:leaves-api:${version}"
    }

    String bungeeCordApi(String version) {
        return "net.md-5:bungeecord-api:${version}"
    }
}
