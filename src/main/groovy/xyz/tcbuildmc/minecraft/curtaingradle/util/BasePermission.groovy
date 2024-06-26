package xyz.tcbuildmc.minecraft.curtaingradle.util

enum BasePermission {
    OP("op"),
    NOT_OP("not op"),
    TRUE("true"),
    FALSE("false");

    private final String id

    BasePermission(String id) {
        this.id = id
    }

    String getId() {
        return this.id
    }
}