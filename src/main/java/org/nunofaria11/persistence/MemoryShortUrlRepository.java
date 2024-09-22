package org.nunofaria11.persistence;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.nunofaria11.entities.ShortUrl;

import java.util.HashMap;

@ApplicationScoped
public class MemoryShortUrlRepository implements ShortUrlRepository {

    private final HashMap<String, ShortUrl> urls = new HashMap<>();

    @Override
    public Uni<ShortUrl> getUrl(String hash) {
        if (urls.containsKey(hash)) {
            return Uni.createFrom().item(urls.get(hash));
        } else {
            return Uni.createFrom().nullItem();
        }
    }

    @Override
    public Uni<ShortUrl> saveUrl(ShortUrl shortUrl) {
        urls.put(shortUrl.hash(), shortUrl);
        return Uni.createFrom().item(shortUrl);
    }
}
