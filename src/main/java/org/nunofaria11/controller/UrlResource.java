package org.nunofaria11.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;

@Path("")
public class UrlResource {

    @GET
    @Path("/{hash}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response get(final String hash) {
        return Response.status(Response.Status.MOVED_PERMANENTLY)
                .location(URI.create("https://www.google.com"))
                .build();
    }
}
