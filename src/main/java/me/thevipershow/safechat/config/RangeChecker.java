package me.thevipershow.safechat.config;

@FunctionalInterface
public interface RangeChecker<T> {
    boolean isInRange(T subject);
}
