package xyz.tcbuildmc.minecraft.curtaingradle.util

enum BasePermission {
    OP,
    NOT_OP,
    TRUE,
    FALSE;

    @Override
    String toString() {
        switch (this) {
            case OP:
                return "op"
            case NOT_OP:
                return "not op"
            case TRUE:
                return "true"
            case FALSE:
                return "false"
            default:
                throw new IllegalArgumentException()
        }
    }
}
