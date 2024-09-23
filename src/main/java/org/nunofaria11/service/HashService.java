package org.nunofaria11.service;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Random;

@ApplicationScoped
public class HashService {

    private static final Random RANDOM = new Random();
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final int LENGTH = 6;

    public String generate() {
        final StringBuilder builder = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            builder.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return builder.toString();
    }
}
