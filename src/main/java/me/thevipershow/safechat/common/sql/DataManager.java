/*
 * SafeChat - A Minecraft plugin to keep your chat safe.
 * Copyright (C) 2020 TheViperShow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.thevipershow.safechat.common.sql;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.thevipershow.safechat.common.configuration.AbstractValues;
import me.thevipershow.safechat.common.configuration.objects.ExecutableObject;
import me.thevipershow.safechat.common.sql.data.Flag;
import me.thevipershow.safechat.common.sql.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataManager {
    @Getter
    private final Set<PlayerData> playerData = new CopyOnWriteArraySet<>();
    private static DataManager instance;

    public static synchronized DataManager getInstance() {
        if (instance == null)
            instance = new DataManager();
        return instance;
    }

    public boolean addPlayerData(PlayerData data) {
        return playerData.add(data);
    }

    /**
     * This method increases the flag value of a player by 1 if the player is found
     * does nothing otherwise.
     * @param flag The flag to increase.
     * @param uuid The unique identifier of the player.
     * @return true if anything was changed, false otherwise.
     */
    public boolean increasePlayerData(Flag flag, UUID uuid) {
        Optional<PlayerData> o = playerData.parallelStream()
                .filter(data -> data.getUuid().equals(uuid))
                .findAny();
        if (!o.isPresent())
            return false;
        o.get().increaseFlag(flag);
        return true;
    }

    /**
     * This methods resets all the data of a player if it is found
     * @param uuid The unique identifier of the player.
     * @return true if anything was changed, false otherwise.
     */
    public boolean resetPlayerData(UUID uuid) {
        Optional<PlayerData> o = playerData.parallelStream()
                .filter(data -> data.getUuid().equals(uuid))
                .findAny();
        if (!o.isPresent())
            return false;
        o.get().resetAllFlags();
        return true;
    }

    /**
     * This method takes as input a list of String and dispatches them from a ConsoleCommandSender.
     * Every message will have the '%PLAYER%' placeholder which will e replaced by the username.
     * @param list The list of commands.
     * @param username The username that will be used to replace placeholders.
     * @param c The ConsoleCommandSender which will execute commands.
     */
    public static void dispatchAndReplace(List<String> list, String username, ConsoleCommandSender c) {
        list.forEach(s -> Bukkit.dispatchCommand(c, s.replaceAll("%PLAYER%", username)));
    }

    /**
     * This method checks a PlayerData for a specific flag and dispatches found commands
     * if the flags had the same value of any of the ExecutableObject flags
     * @param p The PlayerData
     * @param values An AbstractValues implementation.
     * @param c The ConsoleCommandSender which will execute commands.
     * @param of The flag that will be used {@link Flag}.
     */
    public static void executeExecutables(PlayerData p, AbstractValues values, ConsoleCommandSender c, Flag of) {
        int flag = p.getFlags().get(of);
        for (final ExecutableObject e : values.getExecutableOf(of))
            if (e.getFlags() == flag)
                dispatchAndReplace(e.getCommands(), p.getUsername(), c);
    }

    /**
     * This method is used to check if any player is contained in the Set of PlayerData in this class,
     * if one matching is found, the method {@link #executeExecutables(PlayerData, AbstractValues, ConsoleCommandSender, Flag)}
     * will be called in order to check for matching flags and eventually dispatch commands.
     * @param uuid The unique identifier of the player.
     * @param abstractValues An AbstractValues implementation.
     * @param c The ConsoleCommandSender which will execute commands.
     * @param of The flag that will be used {@link Flag}.
     */
    public void checkPlayerAndExecute(UUID uuid, AbstractValues abstractValues, ConsoleCommandSender c, Flag of) {
        playerData.parallelStream()
                .filter(data -> data.getUuid().equals(uuid))
                .findAny()
                .ifPresent(playerData -> executeExecutables(playerData, abstractValues, c, of));
    }
}