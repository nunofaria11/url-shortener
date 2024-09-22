package org.nunofaria11.service.utils;

import java.util.Random;

public final class HashUtils {

    private static final Random RANDOM = new Random();
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyz0123456789";

    private HashUtils() {
        // empty
    }

    public static String hash(final int length) {
        final StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return builder.toString();
    }

}
