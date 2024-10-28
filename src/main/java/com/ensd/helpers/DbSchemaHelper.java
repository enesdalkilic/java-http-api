package com.ensd.helpers;

import java.util.Set;

public class DbSchemaHelper {
    public static <T extends Enum<T>> Boolean validateSchema(T[] e, Set<?> set, boolean useStrict) {

        boolean validated = false;
        boolean missingKey = false;

        for (T key : e) {
            if (!set.contains(key.toString())) {
                missingKey = true;
                break;
            }
        }

        if (!missingKey) validated = true;

        if (useStrict) {
            if (set.size() != e.length) {
                validated = false;
            }
        }

        return validated;
    }
}
