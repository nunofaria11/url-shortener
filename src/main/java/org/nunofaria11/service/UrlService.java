package org.nunofaria11.service;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UrlService {

    public Uni<String> getUrl(final String hash) {
        return Uni.createFrom().item("https://www.google.com");
    }
}
