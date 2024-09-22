package org.nunofaria11.persistence.mongodb;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepositoryBase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import org.nunofaria11.models.ShortUrl;
import org.nunofaria11.persistence.ShortUrlRepository;
import org.nunofaria11.persistence.mongodb.models.ShortUrlEntity;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Date;

@ApplicationScoped
public class MongoShortUrlRepository implements ReactivePanacheMongoRepositoryBase<ShortUrlEntity, String>, ShortUrlRepository {

    private static final Logger LOGGER = Logger.getLogger(MongoShortUrlRepository.class);

    @Override
    public Uni<ShortUrl> getShortUrl(String hash) {
        LOGGER.debugf("Retrieving URL for hash: %s", hash);
        return findById(hash)
                .onItem()
                .ifNotNull()
                .transform(MongoShortUrlRepository::toShortUrl);
    }

    @Override
    public Uni<ShortUrl> persistShortUrl(ShortUrl shortUrl) {
        LOGGER.debugf("Saving URL: %s", shortUrl);
        return persist(toShortUrlEntity(shortUrl))
                .onItem().transform(entity -> shortUrl);
    }

    @Override
    public Uni<ShortUrl> visitShortUrl(String hash) {
        LOGGER.debugf("Visiting URL for hash: %s", hash);
        long changed = ShortUrlEntity.incrementVisits(hash);
        if (changed == 0) {
            return Uni.createFrom().nullItem();
        }
        return this.getShortUrl(hash);
    }

    // Mappers

    private static ShortUrl toShortUrl(ShortUrlEntity entity) {
        try {
            return new ShortUrl(entity.getHash(), URI.create(entity.getUrl()).toURL(), entity.getCreationDate(), entity.getVisits());
        } catch (MalformedURLException e) {
            LOGGER.errorf("Error occurred while parsing URL: %s", entity.getUrl());
            throw new RuntimeException(e);
        }
    }

    private static ShortUrlEntity toShortUrlEntity(ShortUrl shortUrl) {
        ShortUrlEntity entity = new ShortUrlEntity();
        entity.setHash(shortUrl.hash());
        entity.setUrl(shortUrl.url().toString());
        entity.setCreationDate(new Date());
        return entity;
    }
}
