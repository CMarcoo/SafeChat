/*
 * The MIT License
 *
 * Copyright 2020 marco.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package me.thevipershow.safechat.checks;

import me.thevipershow.safechat.config.Values;
import me.thevipershow.safechat.events.FlagThrownEvent;
import me.thevipershow.spigotchatlib.chat.TextMessage;
import me.thevipershow.spigotchatlib.chat.builders.HoverMessageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class DomainsCheck implements ChatCheck {

    private final Values values;

    private static DomainsCheck instance = null;

    private DomainsCheck(Values values) {
        this.values = values;
    }

    public static DomainsCheck getInstance(Values values) {
        if (instance == null) {
            instance = new DomainsCheck(values);
        }
        return instance;
    }

    @Override
    public void result(final String message, final AsyncPlayerChatEvent chatEvent) {

        final String stringToCheck = message.replaceAll(values.getDomainWhitelist(), "");
        boolean result = stringToCheck.matches(values.getDomainRegex());

        if (result && !chatEvent.isCancelled()) {
            final Player player = chatEvent.getPlayer();
            chatEvent.setCancelled(true);
            Bukkit.getPluginManager().callEvent(new FlagThrownEvent(1, "domains", player.getUniqueId(), player.getName()));
            chatEvent.getPlayer().spigot().sendMessage(HoverMessageBuilder.buildHover(
                    TextMessage.build(values.getArrayAndReplace(values.getDomainWarning(), "%PLAYER%", player.getName())).color(),
                    TextMessage.build(values.getArrayAndReplace(values.getDomainHover(), "%PLAYER%", player.getName())).color()
            ));
        }
    }
}
