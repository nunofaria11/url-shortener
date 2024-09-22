package org.nunofaria11.service;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.nunofaria11.entities.ShortUrl;
import org.nunofaria11.service.utils.HashUtils;

import java.net.URI;
import java.util.HashMap;

@ApplicationScoped
public class UrlService {

    private final HashMap<String, ShortUrl> urls = new HashMap<>();

    public Uni<ShortUrl> getUrl(final String hash) {
        ShortUrl shortUrl = urls.get(hash);
        if (shortUrl == null) {
            return Uni.createFrom().nullItem();
        }
        return Uni.createFrom().item(shortUrl);
    }

    public Uni<ShortUrl> saveUrl(final URI url) {
        final String hash = generateNonExistingHash();
        ShortUrl shortUrl = new ShortUrl(hash, url);
        urls.put(hash, shortUrl);
        return Uni.createFrom().item(shortUrl);
    }

    private String generateNonExistingHash() {
        String hash;
        do {
            hash = HashUtils.hash(6);
        } while (urls.containsKey(hash));
        return hash;
    }

}
