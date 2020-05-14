package me.thevipershow.safechat.checks;

import java.util.List;
import java.util.regex.Matcher;
import me.thevipershow.safechat.config.Values;
import me.thevipershow.safechat.config.WordsMatcher;
import me.thevipershow.spigotchatlib.chat.TextMessage;
import me.thevipershow.spigotchatlib.chat.builders.HoverMessageBuilder;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public final class WordsCheck implements ChatCheck {

    private final Values values;

    private static WordsCheck instance = null;

    private WordsCheck(Values values) {
        this.values = values;
    }

    public static WordsCheck getInstance(Values values) {
        return instance != null ? instance : (instance = new WordsCheck(values));
    }

    private String joinStrings(final List<String> list) {
        final StringBuilder builder = new StringBuilder();
        list.forEach(s -> builder.append(s + ","));
        return builder.toString();
    }

    @Override
    public final void result(String message, AsyncPlayerChatEvent chatEvent) {

        String adaptedString = message;
        boolean sendWarning = false;
        boolean sendModifiedMessage = false;


        if (values.isWordsCancelEvent()) {
            for (WordsMatcher wordsMatcher : values.getBlacklistWords()) {
                if (message.matches(wordsMatcher.getPattern())) {
                    sendWarning = true;
                    break;
                }
            }
        } else {
            for (WordsMatcher wordsMatcher : values.getBlacklistWords()) {
                final Matcher matcher = wordsMatcher.getCompiledPattern().matcher(adaptedString);
                if (matcher.matches()) {
                    if (wordsMatcher.getReplace() != null) {
                        adaptedString = matcher.replaceAll(wordsMatcher.getReplace());
                    }
                    sendModifiedMessage = true;
                    sendWarning = true;
                }
            }
        }

        if (sendWarning) {
            chatEvent.getPlayer().spigot().sendMessage(HoverMessageBuilder.buildHover(
                    TextMessage.build(values.getArrayAndReplace(values.getWordsWarning(), "%PLAYER%", chatEvent.getPlayer().getName())).color(),
                    TextMessage.build(values.getArrayAndReplace(values.getWordsHover(), "%PLAYER%", chatEvent.getPlayer().getName())).color()
            ));
        }
        if (sendModifiedMessage) {
            chatEvent.setCancelled(true);
            chatEvent.getPlayer().chat(adaptedString);
        }
    }
}
