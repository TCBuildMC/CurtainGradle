package xyz.tcbuildmc.gradle.minecraft.horizontal_gradle.runserver.util

import com.fasterxml.jackson.databind.json.JsonMapper

class UrlBuilder {
    private static final JsonMapper mapper = new JsonMapper()

    static String papermc(String project, String version, int build) {
        return "https://api.papermc.io/v2/projects/${project}/versions/${version}/builds/${build}/downloads/${project}-${version}-${build}.jar"
    }

    static String papermc(String project, String version) {
        def map = mapper.readValue(new URI("https://api.papermc.io/v2/projects/${project}/versions/${version}/builds").toURL(), Map<String, ?>.class) as Map<String, ?>

        def builds = map.get("builds") as List<Map<String, ?>>
        def id = builds.last().getOrDefault("build", 1) as int
        return papermc(project, version, id)
    }

    static String paper(String version) {
        return papermc("paper", version)
    }

    static String travertine(String version) {
        return papermc("travertine", version)
    }

    static String waterfall(String version) {
        return papermc("waterfall", version)
    }

    static String velocity(String version) {
        return papermc("velocity", version)
    }

    static String folia(String version) {
        return papermc("folia", version)
    }

    static String purpur(String version, int build) {
        return "https://api.purpurmc.org/v2/purpur/${version}/${build}/download"
    }

    static String purpur(String version) {
        return "https://api.purpurmc.org/v2/purpur/${version}/latest/download"
    }

    static String leavesmc(String project, String version, int build) {
        return "https://api.leavesmc.org/v2/projects/${project}/versions/${version}/builds/${build}/downloads/${project}-${version}-${build}.jar"
    }

    static String leavesmc(String project, String version) {
        def map = mapper.readValue(new URI("https://api.leavesmc.org/v2/projects/${project}/versions/${version}/builds").toURL(), Map<String, ?>.class) as Map<String, ?>

        def builds = map.get("builds") as List<Map<String, ?>>
        def id = builds.last().getOrDefault("build", 1) as int
        return leavesmc(project, version, id)
    }

    static String leaves(String version) {
        return leavesmc("leaves", version)
    }

    static String lumina(String version) {
        return leavesmc("lumina", version)
    }

    static String mohistmc(String project, String version, int build) {
        return "https://mohistmc.com/api/v2/projects/${project}/${version}/builds/${build}/download"
    }

    static String mohistmc(String project, String version) {
        def map = mapper.readValue(new URI("https://mohistmc.com/api/v2/projects/${project}/${version}/builds").toURL(), Map<String, ?>.class) as Map<String, ?>

        def builds = map.get("builds") as List<Map<String, ?>>
        return builds.last().get("url") as String
    }

    static String mohist(String version) {
        return mohistmc("mohist", version)
    }
}
