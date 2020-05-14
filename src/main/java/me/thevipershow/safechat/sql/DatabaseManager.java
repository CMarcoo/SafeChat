package me.thevipershow.safechat.sql;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface DatabaseManager {
    void createTable(final ExceptionHandler handler);

    void addUniquePlayerOrUpdate(final UUID playerUuid, final int severity, final ExceptionHandler handler);

    CompletableFuture<Integer> getPlayerData(final UUID playerUuid);

    CompletableFuture<Map<String, Integer>> getTopData(final int search);
}
