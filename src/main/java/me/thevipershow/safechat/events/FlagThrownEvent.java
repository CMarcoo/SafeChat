package me.thevipershow.safechat.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class FlagThrownEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final int severity;
    private final String checkName;
    private final UUID senderUUID;

    public FlagThrownEvent(final int severity, final String checkName, final UUID senderUUID) {
        super(true);
        this.severity = severity;
        this.checkName = checkName;
        this.senderUUID = senderUUID;

    }

    public int getSeverity() {
        return severity;
    }

    public String getCheckName() {
        return checkName;
    }

    public UUID getSenderUUID() {
        return senderUUID;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
