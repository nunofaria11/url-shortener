package org.nunofaria11.persistence.mongodb.models;

import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonId;

import java.util.Date;
import java.util.Objects;

@MongoEntity(collection = "short_urls")
public class ShortUrlEntity extends PanacheMongoEntityBase {
    @BsonId
    private String hash;
    private String url;
    private Date creationDate;
    private long visits;

    public static long incrementVisits(String hash) {
        return update("{'$inc': {'visits': 1}}").where("_id = ?1", hash);
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public long getVisits() {
        return visits;
    }

    public void setVisits(long visits) {
        this.visits = visits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShortUrlEntity that = (ShortUrlEntity) o;
        return visits == that.visits && Objects.equals(hash, that.hash) && Objects.equals(url, that.url) && Objects.equals(creationDate, that.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hash, url, creationDate, visits);
    }
}
