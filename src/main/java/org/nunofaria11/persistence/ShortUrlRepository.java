package org.nunofaria11.persistence;

import io.smallrye.mutiny.Uni;
import org.nunofaria11.models.ShortUrl;

public interface ShortUrlRepository {

    Uni<ShortUrl> getShortUrl(final String hash);

    Uni<ShortUrl> persistShortUrl(final ShortUrl shortUrl);

    Uni<ShortUrl> visitShortUrl(final String hash);
}
