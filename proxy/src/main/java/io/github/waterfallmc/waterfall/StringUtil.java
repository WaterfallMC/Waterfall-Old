package io.github.waterfallmc.waterfall;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtil {
    public static boolean isBlank(String str) {
        if (str.isEmpty()) {
            return true;
        }

        int l = str.length();
        for (int i = 0; i < l; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }
}
