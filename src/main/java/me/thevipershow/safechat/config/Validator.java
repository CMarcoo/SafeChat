package me.thevipershow.safechat.config;

import me.thevipershow.safechat.sql.ExceptionHandler;
import org.yaml.snakeyaml.error.YAMLException;

public final class Validator {
    public static <T> boolean validate(Object subject, ExceptionHandler handler, T... validOptions) {
        for (T t : validOptions) {
            if (t.equals(subject)) {
                return true;
            }
        }
        final YAMLException e = new YAMLException("The value was invalid »" + subject.toString());
        handler.handle(e);
        throw e;
    }

    @FunctionalInterface
    public interface RangeChecker<T> {
        boolean isInRange(T subject);
    }

    public abstract static class Range<T extends Comparable<T>> implements RangeChecker<T> {
        protected final T lowerBound;
        protected final T upperBound;

        public Range(T lowerBound, T upperBound) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        }
    }

    public static class NumberRange<N extends Number & Comparable<N>> extends Range<N> {

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

    public static <N extends Number & Comparable<N>> boolean validateInRange(N number, ExceptionHandler handler, NumberRange<N> range) {
        if (range.isInRange(number)) {
            return true;
        }
        final YAMLException e = new YAMLException("The number is outside valid range [" + range.lowerBound + "-" + range.upperBound + "]");
        handler.handle(e);
        throw e;
    }

    public static boolean validateNotNull(Object o, ExceptionHandler handler) {
        if (o != null) {
            return true;
        }
        final YAMLException e = new YAMLException("List inside the config.yml can't be null!");
        handler.handle(e);
        throw e;
    }
}
