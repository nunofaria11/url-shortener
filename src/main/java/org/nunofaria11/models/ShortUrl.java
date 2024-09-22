package org.nunofaria11.models;

import java.net.URL;
import java.util.Date;

public record ShortUrl(String hash, URL url, Date creationDate, long visits) {
}
