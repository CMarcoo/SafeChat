package me.thevipershow.safechat.enums;

public enum SPermissions {

    BYPASS("safechat.bypass"),
    COMMAND("safechat.command");

    private final String string;

    SPermissions(String string) {
        this.string = string;
    }

    public final String getConcatPermission(final String permission) {
        return string.concat(".".concat(permission));
    }

    public final String getPermission() {
        return string;
    }
}
