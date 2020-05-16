/*
 * SafeChat - A Minecraft plugin to keep your chat safe.
 *  Copyright (C) 2020 TheViperShow
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
        boolean autoSaveCheck = Validator.validateInRange(values.getAutoSave(), Throwable::printStackTrace, NumberRange.process(1, Integer.MAX_VALUE));
        boolean portCheck = Validator.validateInRange(values.getPort(), Throwable::printStackTrace, NumberRange.process(0, 65535));
        boolean nullCheckBlacklistWords = Validator.validateNotNull(values.getBlacklistWords(), Throwable::printStackTrace);
        boolean nullCheckDomainHover = Validator.validateNotNull(values.getDomainHover(), Throwable::printStackTrace);
        boolean nullCheckDomainWarning = Validator.validateNotNull(values.getDomainWarning(), Throwable::printStackTrace);
        boolean nullCheckWordsWarning = Validator.validateNotNull(values.getWordsWarning(), Throwable::printStackTrace);
        boolean nullCheckWordsHover = Validator.validateNotNull(values.getWordsHover(), Throwable::printStackTrace);
        return databaseType && autoSaveCheck && portCheck && nullCheckBlacklistWords && nullCheckDomainHover && nullCheckDomainWarning && nullCheckWordsHover && nullCheckWordsWarning;
    }
}
