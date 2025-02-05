package com.example.catalog.controllers;

import com.example.catalog.model.Song;
import com.example.catalog.services.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/songs")
public class SongController {

    private final DataSourceService dataSourceService;

    @Autowired
    public SongController(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs() throws IOException {
        List<Song> songs = dataSourceService.getAllSongs();
        if (songs == null || songs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 404 Not Found if no songs are found
        }

        return ResponseEntity.ok(songs);  // 200 OK if songs are found
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable String id) throws IOException {
        if (!isValidId(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();  // 400 Bad Request if ID is invalid
        }

        Song song = dataSourceService.getSongById(id);
        if (song == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 404 Not Found if song does not exist
        }

        return ResponseEntity.ok(song);  // 200 OK if song is found
    }

    @PostMapping
    public ResponseEntity<Void> addSong(@RequestBody Song song) throws IOException {
        if (song == null || song.getName() == null || song.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();  // 400 Bad Request if song data is invalid
        }

        try {
            dataSourceService.addSong(song);
            return ResponseEntity.status(HttpStatus.CREATED).build();  // 201 Created if song is added
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // 500 Internal Server Error
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSong(@PathVariable String id, @RequestBody Song updatedSong) throws IOException {
        if (!isValidId(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();  // 400 Bad Request if ID is invalid
        }

        Song existingSong = dataSourceService.getSongById(id);
        if (existingSong == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 404 Not Found if song does not exist
        }

        try {
            dataSourceService.updateSong(id, updatedSong);
            return ResponseEntity.status(HttpStatus.OK).build();  // 200 OK if song is updated
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // 500 Internal Server Error
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable String id) throws IOException {
        if (!isValidId(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();  // 400 Bad Request if ID is invalid
        }

        Song song = dataSourceService.getSongById(id);
        if (song == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 404 Not Found if song does not exist
        }

        try {
            dataSourceService.deleteSong(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // 204 No Content if song is deleted
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // 500 Internal Server Error
        }
    }

    // Helper method for ID validation
    private boolean isValidId(String id) {
        return id != null && !id.isEmpty();  // You can add additional ID validation logic here if necessary
    }
}