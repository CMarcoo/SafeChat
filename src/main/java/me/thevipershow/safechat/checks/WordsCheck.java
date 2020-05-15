package me.thevipershow.safechat.checks;

import java.util.regex.Matcher;
import me.thevipershow.safechat.config.Values;
import me.thevipershow.safechat.config.WordsMatcher;
import me.thevipershow.safechat.events.FlagThrownEvent;
import me.thevipershow.spigotchatlib.chat.TextMessage;
import me.thevipershow.spigotchatlib.chat.builders.HoverMessageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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

    @Override
    public final void result(String message, AsyncPlayerChatEvent chatEvent) {
        String adaptedMessage = message;
        final Player player = chatEvent.getPlayer();
        short flags = 0;
        short replaced = 0;
        for (final WordsMatcher wordsMatcher : values.getBlacklistWords()) {
            final Matcher matcher = wordsMatcher.getCompiledPattern().matcher(adaptedMessage);
            if (matcher.matches()) {
                flags++;
                final String replace = wordsMatcher.getReplace();
                if (!replace.equals("NONE")) {
                    replaced++;
                    adaptedMessage = matcher.replaceAll(replace);
                }
            }
        }
        if (flags > 0) {
            Bukkit.getPluginManager().callEvent(new FlagThrownEvent(flags, "domains", player.getUniqueId(), player.getName()));

            chatEvent.getPlayer().spigot().sendMessage(HoverMessageBuilder.buildHover(
                    TextMessage.build(values.getArrayAndReplace(values.getDomainWarning(), "%PLAYER%", player.getName())).color(),
                    TextMessage.build(values.getArrayAndReplace(values.getDomainHover(), "%PLAYER%", player.getName())).color()
            ));
            if (replaced > 0) {
                chatEvent.setCancelled(true);
                player.chat(adaptedMessage);
            }
        }
    }
}
