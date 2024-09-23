package org.nunofaria11.service;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.nunofaria11.models.ShortUrl;
import org.nunofaria11.persistence.ShortUrlRepository;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@QuarkusTest
public class UrlShortenerServiceTest {

    @InjectMock
    ShortUrlRepository shortUrlRepository;

    @InjectMock
    HashService hashService;

    @Inject
    UrlShortenerService urlShortenerService;

    @Test
    void testShorten() throws MalformedURLException {
        // given
        Mockito.when(hashService.generate()).thenReturn("abc123");
        Mockito.when(shortUrlRepository.getShortUrl("abc123")).thenReturn(Uni.createFrom().nullItem());
        Mockito.when(shortUrlRepository.persistShortUrl(Mockito.any(ShortUrl.class))).thenReturn(
                Uni.createFrom().item(new ShortUrl("abc123", URI.create("http://example.com").toURL(), new Date(), 0)));

        // when
        ShortUrl actual = urlShortenerService.shorten(URI.create("http://example.com").toURL())
                .await().atMost(Duration.ofSeconds(2));

        // then
        Assertions.assertEquals("abc123", actual.hash());
        Assertions.assertEquals("http://example.com", actual.url().toString());
        Assertions.assertEquals(0, actual.visits());
    }

    @Test
    void testShorten_ExistingHash() throws MalformedURLException {
        // given
        Mockito.when(hashService.generate()).thenReturn("abc123").thenReturn("def456");
        Mockito.when(shortUrlRepository.getShortUrl("abc123")).thenReturn(Uni.createFrom().item(new ShortUrl("abc123",
                URI.create("http://example.com").toURL(), new Date(), 0)));
        Mockito.when(shortUrlRepository.getShortUrl("abc123")).thenReturn(Uni.createFrom().nullItem());
        Mockito.when(shortUrlRepository.persistShortUrl(Mockito.any(ShortUrl.class))).thenReturn(
                Uni.createFrom().item(new ShortUrl("def456", URI.create("http://example.com").toURL(), new Date(), 0)));

        // when
        ShortUrl actual = urlShortenerService.shorten(URI.create("http://example.com").toURL())
                .await().atMost(Duration.ofSeconds(2));

        // then
        Assertions.assertEquals("def456", actual.hash());
        Assertions.assertEquals("http://example.com", actual.url().toString());
        Assertions.assertEquals(0, actual.visits());
    }

    // Only the shorten method is tested, since it is the only one that has logic.
}
