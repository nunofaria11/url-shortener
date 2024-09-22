# url-shortener

A simple URL shortener service built with Quarkus.

To run the application locally
- run the `docker-compose.yaml` to set up a local MongoDB instance
- the [README.quarkus.md](README.quarkus.md) file should be followed

## API

### POST /

Shortens a URL.

#### Request

```json
{
  "url": "https://www.google.com"
}
```

#### Response

```json
{
  "url": "https://www.google.com",
  "shortUrl": "http://localhost:8080/{hash}"
}
```

### GET /{hash}

Redirects to the original URL using a 301 Moved Permanently status code with the location header set to the original
URL.

### GET /info/{hash}

Returns information about the shortened URL.

#### Response

```json
{
  "url": "https://www.google.com",
  "shortUrl": "http://localhost:8080/{hash}"
}
```
