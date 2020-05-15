package me.thevipershow.safechat.config;

import me.thevipershow.safechat.sql.ExceptionHandler;
import org.yaml.snakeyaml.error.YAMLException;

public final class Validator {

    @SafeVarargs
    public static <T> boolean validate(T subject, ExceptionHandler handler, T... validOptions) {
        for (T t : validOptions) {
            if (t.equals(subject)) {
                return true;
            }
        }
        final YAMLException e = new YAMLException("The value was invalid »" + subject.toString());
        handler.handle(e);
        throw e;
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
