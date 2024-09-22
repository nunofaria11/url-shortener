package org.nunofaria11.service;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.nunofaria11.models.ShortUrl;
import org.nunofaria11.persistence.ShortUrlRepository;
import org.nunofaria11.service.utils.HashUtils;

import java.net.URL;
import java.util.Date;

@ApplicationScoped
public class UrlShortenerService {

    @Inject
    ShortUrlRepository urlShortenerRepository;

    public Uni<ShortUrl> get(final String hash) {
        return urlShortenerRepository.getShortUrl(hash);
    }

    public Uni<ShortUrl> save(final URL url) {
        return generateNonExistingHash()
                .onItem().transform(hash -> new ShortUrl(hash, url, new Date(), 0))
                .onItem().transformToUni(urlShortenerRepository::persistShortUrl);
    }

    public Uni<ShortUrl> visit(final String hash) {
        return urlShortenerRepository.visitShortUrl(hash);
    }

    private Uni<String> generateNonExistingHash() {
        String hash = HashUtils.hash(6);
        return urlShortenerRepository.getShortUrl(hash)
                .onItem().transformToUni(url -> {
                    if (url == null) {
                        return Uni.createFrom().item(hash);
                    } else {
                        return generateNonExistingHash();
                    }
                });
    }

}
