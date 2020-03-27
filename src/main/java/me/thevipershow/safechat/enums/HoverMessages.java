package me.thevipershow.safechat.enums;

public enum HoverMessages {

    NO_ARGS("&eSafeChat &7syntax:",
            "&6Open this help page&7:",
            "- &7/safechat",
            "&6Get stored data of a certain player&7:",
            "- &7/safechat sql search &8<&eplayer&8>",
            "&6Get &o&nX&r &6players with the highest flags count&7:",
            "- &7/safechat sql top &8<&enumber&8>");

    private final String[] messages;

    HoverMessages(String... messages) {
        this.messages = messages;
    }

    public String[] getMessages() {
        return messages;
    }
}
