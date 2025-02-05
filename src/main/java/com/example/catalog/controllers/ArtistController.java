package com.example.catalog.controllers;

import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.example.catalog.model.Song;
import com.example.catalog.services.DataSourceService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    private final DataSourceService dataSourceService;

    @Autowired
    public ArtistController(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artist> getArtistById(@PathVariable String id) throws IOException {
        if (!isValidId(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);  // 400 Bad Request if ID is invalid
        }

        Artist artist = dataSourceService.getArtistById(id);
        if (artist == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // 404 Not Found if artist does not exist
        }

        return ResponseEntity.ok(artist);  // 200 OK if artist is found
    }

    @GetMapping
    public ResponseEntity<List<Artist>> getAllArtists() throws IOException {
        List<Artist> artists = dataSourceService.getAllArtists();
        if (artists == null || artists.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // 404 Not Found if no artists are found
        }

        return ResponseEntity.ok(artists);  // 200 OK if artists are found
    }

    @PostMapping
    public ResponseEntity<Void> addArtist(@RequestBody Artist artist) {
        try {
            if (artist == null || artist.getName() == null || artist.getName().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();  // 400 Bad Request if artist data is invalid
            }

            dataSourceService.addArtist(artist);
            return ResponseEntity.status(HttpStatus.CREATED).build();  // 201 Created if artist is added
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // 500 Internal Server Error
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateArtist(@PathVariable String id, @RequestBody Artist artist) {
        try {
            if (!isValidId(id)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();  // 400 Bad Request if ID is invalid
            }

            Artist existingArtist = dataSourceService.getArtistById(id);
            if (existingArtist == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 404 Not Found if artist does not exist
            }

            dataSourceService.updateArtist(id, artist);
            return ResponseEntity.status(HttpStatus.OK).build();  // 200 OK if artist is updated
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // 500 Internal Server Error
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtist(@PathVariable String id) {
        try {
            if (!isValidId(id)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();  // 400 Bad Request if ID is invalid
            }

            Artist artist = dataSourceService.getArtistById(id);
            if (artist == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 404 Not Found if artist does not exist
            }

            dataSourceService.deleteArtist(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // 204 No Content if artist is deleted
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // 500 Internal Server Error
        }
    }

    @GetMapping("/{id}/albums")
    public ResponseEntity<List<Album>> getAlbumsByArtist(@PathVariable String id) throws IOException {
        if (!isValidId(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();  // 400 Bad Request if ID is invalid
        }

        List<Album> albums = dataSourceService.getAlbumsByArtist(id);
        if (albums.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found if no albums are found
        }
        return ResponseEntity.ok(albums); // 200 OK if albums are found
    }

    @GetMapping("/{id}/songs")
    public ResponseEntity<List<Song>> getSongsByArtist(@PathVariable String id) throws IOException {
        if (!isValidId(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();  // 400 Bad Request if ID is invalid
        }

        List<Song> songs = dataSourceService.getSongsByArtist(id);
        if (songs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found if no songs are found
        }
        return ResponseEntity.ok(songs); // 200 OK if songs are found
    }

    private boolean isValidId(String id) {
        return id != null && !id.isEmpty();  // You can add additional ID validation logic here
    }
}