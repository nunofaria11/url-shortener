package org.nunofaria11.service;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.nunofaria11.entities.ShortUrl;
import org.nunofaria11.persistence.ShortUrlRepository;
import org.nunofaria11.service.utils.HashUtils;

import java.net.URI;

@ApplicationScoped
public class UrlShortenerService {

    @Inject
    ShortUrlRepository urlShortenerRepository;

    public Uni<ShortUrl> getUrl(final String hash) {
        return urlShortenerRepository.getUrl(hash);
    }

    public Uni<ShortUrl> saveUrl(final URI url) {
        return generateNonExistingHash()
                .onItem().transform(hash -> new ShortUrl(hash, url))
                .onItem().transformToUni(urlShortenerRepository::saveUrl);
    }

    private Uni<String> generateNonExistingHash() {
        String hash = HashUtils.hash(6);
        return urlShortenerRepository.getUrl(hash)
                .onItem().transformToUni(existingUrl -> {
                    if (existingUrl == null) {
                        return Uni.createFrom().item(hash);
                    } else {
                        return generateNonExistingHash();
                    }
                });
    }

}
