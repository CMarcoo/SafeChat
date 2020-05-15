package me.thevipershow.safechat.config;

public class NumberRange<N extends Number & Comparable<N>> extends Range<N> {

    public NumberRange(N lowerBound, N upperBound) {
        super(lowerBound, upperBound);
    }

    public static <T extends Number & Comparable<T>> NumberRange<T> process(T lowerLimit, T upperLimit) {
        return new NumberRange<>(lowerLimit, upperLimit);
    }

    @Override
    public boolean isInRange(N subject) {
        return (subject.compareTo(super.lowerBound) >= 0) && (subject.compareTo(super.upperBound) <= 0);
    }
}
