package me.thevipershow.safechat.config;

import java.util.Locale;

public final class ValuesValidator {

    public static ValuesValidator instance = null;
    private final Values values;

    private ValuesValidator(final Values values) {
        this.values = values;
    }

    public static ValuesValidator getInstance(final Values values) {
        return instance != null ? instance : (instance = new ValuesValidator(values));
    }

    public boolean validateAll() {
        boolean databaseType = Validator.validate(values.getDbType().toUpperCase(Locale.getDefault()), Throwable::printStackTrace, "SQLITE", "POSTGRESQL", "MYSQL");
        boolean portCheck = Validator.validateInRange(values.getPort(), Throwable::printStackTrace, NumberRange.process(0, 65535));
        boolean nullCheckBlacklistWords = Validator.validateNotNull(values.getBlacklistWords(), Throwable::printStackTrace);
        boolean nullCheckDomainHover = Validator.validateNotNull(values.getDomainHover(), Throwable::printStackTrace);
        boolean nullCheckDomainWarning = Validator.validateNotNull(values.getDomainWarning(), Throwable::printStackTrace);
        boolean nullCheckWordsWarning = Validator.validateNotNull(values.getWordsWarning(), Throwable::printStackTrace);
        boolean nullCheckWordsHover = Validator.validateNotNull(values.getWordsHover(), Throwable::printStackTrace);
        return databaseType && portCheck && nullCheckBlacklistWords && nullCheckDomainHover && nullCheckDomainWarning && nullCheckWordsHover && nullCheckWordsWarning;
    }
}
