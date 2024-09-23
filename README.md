# url-shortener

A simple URL shortener service built with Quarkus.

To run the application locally:

- run the `docker-compose.yaml` to set up a local MongoDB instance
- the [README.quarkus.md](README.quarkus.md) file should be followed

```shell
docker-compose up -d
./mvnw compile quarkus:dev
```

## Configuration properties

- `url-shortener.redirect-host` - the base to redirect to the original URL
- `url-shortener.hash-length` - the length of the hash (default is 6)
- `quarkus.mongodb.connection-string` - the connection string to the MongoDB instance
- `quarkus.mongodb.database` - the name of the database

## API

### POST /

Shortens a URL.

#### Request

```http request
POST /

{
  "url": "https://www.google.com"
}
```

#### Response

```http response
HTTP/1.1 201 Created
Location: http://localhost:8080/{hash}
```

### GET /{hash}

Redirects to the original URL using a 301 Moved Permanently status code with the location header set to the original
URL.

#### Response

```http request
HTTP/1.1 301 Moved Permanently
Location: https://www.google.com
```

### GET /info/{hash}

Returns information about the shortened URL.

#### Response

```json
{
  "url": "https://www.google.com",
  "shortUrl": "http://localhost:8080/{hash}",
  "createdDate": "2024-09-23T01:18:59.563Z[UTC]",
  "visits": 1
}
```

## Features and Missing Features

Features:

- shortens a URL and saves to a MongoDB instance
- redirects to the original URL via "301 Moved Permanently" status code and Location header
- retrieves information about the shortened URL
- visits counter for each short url "Redirect" done
- the microservice is stateless so it can be scaled horizontally
- the stored entry only holds the hash; if this would be a fully deployed service a single DNS domain would be used, and
  by not storing the full domain name, the service can be moved to another domain without any changes to the stored
  entries

Missing features:

- users and authentication
    - add a "users" MongoDB collection
    - endpoints requiring authentication are: `POST /` and `GET /info/{hash}`
    - add an `/admin` API to manage URLs of each user
- URL expiration
    - add an expiration date to the shortened URL stored in the MongoDB instance (TTL)
- Redis cache
    - used to cache the shortened URLs for faster retrieval
    - used to cache user sessions
    - also handy to increment the visits counter
- HashService randomizer seed should be based on a node identifier (horizontal scaling scenario) to prevent hash
  collisions