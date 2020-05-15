package me.thevipershow.safechat.enums;

public enum ANSIColor {
    BLACK("\u001B[30m", "B"),
    RED("\u001B[31m", "r"),
    GREEN("\u001B[32m", "g"),
    YELLOW("\u001B[33m", "y"),
    BLUE("\u001B[34m", "b"),
    MAGENTA("\u001B[35m", "m"),
    CYAN("\u001B[36m", "c"),
    WHITE("\u001B[37m", "w"),
    BLACK_BACKGROUND("\u001B[40m", "Bb"),
    RED_BACKGROUND("\u001B[41m", "rb"),
    GREEN_BACKGROUND("\u001B[42m", "gb"),
    YELLOW_BACKGROUND("\u001B[43m", "yb"),
    BLUE_BACKGROUND("\u001B[44m", "bb"),
    MAGENTA_BACKGROUND("\u001B[45m", "mb"),
    CYAN_BACKGROUND("\u001B[46m", "cb"),
    WHITE_BACKGROUND("\u001B[47m", "wb"),
    RESET("\u001B[0m", "R");

    final String placeholder;
    final String code;

    ANSIColor(String code, String placeholder) {
        this.code = code;
        this.placeholder = placeholder;
    }

    public String code() {
        return code;
    }

    public static String colorString(final char placeholder, String input) {
        for (final ANSIColor value : values()) {
            input = input.replace(placeholder + value.placeholder, value.code);
        }
        return input;
    }
}
