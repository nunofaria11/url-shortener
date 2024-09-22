package org.nunofaria11.persistence;

import io.smallrye.mutiny.Uni;
import org.nunofaria11.entities.ShortUrl;

public interface ShortUrlRepository {

    Uni<ShortUrl> getUrl(final String hash);

    Uni<ShortUrl> saveUrl(final ShortUrl shortUrl);

}
