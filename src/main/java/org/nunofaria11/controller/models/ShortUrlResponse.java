package org.nunofaria11.controller.models;

import java.net.URL;
import java.util.Date;

public record ShortUrlResponse(URL url, URL shortUrl, Date createdDate, long visits) {
}