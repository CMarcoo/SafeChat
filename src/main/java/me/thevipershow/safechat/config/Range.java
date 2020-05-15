package me.thevipershow.safechat.config;

public abstract class Range<T extends Comparable<T>> implements RangeChecker<T> {
    protected final T lowerBound;
    protected final T upperBound;

    public Range(T lowerBound, T upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }
}
