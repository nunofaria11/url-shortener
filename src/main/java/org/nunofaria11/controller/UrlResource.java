package org.nunofaria11.controller;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.nunofaria11.service.UrlService;

import java.net.URI;

@Path("")
public class UrlResource {

    @Inject
    UrlService urlService;

    @GET
    @Path("/{hash}")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<Response> get(final String hash) {
        return urlService.getUrl(hash)
                .onItem().transform(URI::create)
                .onItem().transform(UrlResource::redirectResponse);
    }

    private static Response redirectResponse(final URI uri) {
        return Response.status(Response.Status.MOVED_PERMANENTLY)
                .location(uri)
                .build();
    }
}
