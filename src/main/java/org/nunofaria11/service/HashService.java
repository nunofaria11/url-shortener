package org.nunofaria11.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Random;

@ApplicationScoped
public class HashService {

    private static final Random RANDOM = new Random();
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyz0123456789";

    @ConfigProperty(name = "url-shortener.hash-length", defaultValue = "6")
    int hashLength;

    public String generate() {
        final StringBuilder builder = new StringBuilder(hashLength);
        for (int i = 0; i < hashLength; i++) {
            builder.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return builder.toString();
    }
}
