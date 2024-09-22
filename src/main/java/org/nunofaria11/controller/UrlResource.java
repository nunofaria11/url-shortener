package org.nunofaria11.controller;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.nunofaria11.controller.models.ShortUrlRequest;
import org.nunofaria11.controller.models.ShortUrlResponse;
import org.nunofaria11.entities.ShortUrl;
import org.nunofaria11.service.UrlService;

import java.net.URI;

@Path("")
public class UrlResource {

    private static final Logger LOGGER = Logger.getLogger(UrlResource.class);

    @Inject
    UrlService urlService;

    @ConfigProperty(name = "url-shortener.redirect-host")
    URI redirectHost;

    @GET
    @Path("/{hash}")
    public Uni<Response> get(final String hash) {
        LOGGER.debugf("Received request for hash %s", hash);
        return urlService.getUrl(hash)
                .onItem()
                .transform(url -> {
                    if (url == null) {
                        LOGGER.warnf("URL not found for hash: %s", hash);
                        return notFoundResponse();
                    }

                    LOGGER.debugf("URL found for hash: %s", hash);
                    return redirectResponse(toShortUrlResponse(url));

                })
                .onFailure()
                .recoverWithItem(throwable -> {
                    LOGGER.errorf("Error occurred while retrieving URL for hash: %s, error: %s", hash, throwable.getMessage());
                    return internalServerErrorResponse();
                });
    }

    @GET
    @Path("/info/{hash}")
    public Uni<Response> info(final String hash) {
        LOGGER.debugf("Received request for hash %s", hash);
        return urlService.getUrl(hash)
                .onItem()
                .transform(url -> {
                    if (url == null) {
                        LOGGER.warnf("URL not found for hash: %s", hash);
                        return notFoundResponse();
                    }

                    LOGGER.debugf("URL found for hash: %s", hash);
                    return successResponse(toShortUrlResponse(url));

                })
                .onFailure()
                .recoverWithItem(throwable -> {
                    LOGGER.errorf("Error occurred while retrieving URL for hash: %s, error: %s", hash, throwable.getMessage());
                    return internalServerErrorResponse();
                });
    }

    @POST
    public Uni<Response> post(final ShortUrlRequest request) {
        LOGGER.debugf("Received request to shorten URL %s", request.url());
        return urlService.saveUrl(request.url())
                .onItem().transform(this::toShortUrlResponse)
                .onItem().transform(UrlResource::successResponse)
                .onFailure()
                .recoverWithItem(throwable -> {
                    LOGGER.errorf("Error occurred while shortening URL %s, error: %s", request.url(), throwable.getMessage());
                    return internalServerErrorResponse();
                });
    }

    // Private methods

    private ShortUrlResponse toShortUrlResponse(final ShortUrl url) {
        var shortUrl = URI.create(redirectHost.toString() + "/" + url.hash());
        return new ShortUrlResponse(url.url(), shortUrl);
    }

    private static Response successResponse(final ShortUrlResponse url) {
        return Response.ok(url).build();
    }

    private static Response redirectResponse(final ShortUrlResponse url) {
        return Response.status(Response.Status.MOVED_PERMANENTLY)
                .location(url.url())
                .build();
    }

    private static Response notFoundResponse() {
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    private static Response internalServerErrorResponse() {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
