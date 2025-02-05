package com.example.catalog.controllers;

import com.example.catalog.model.Album;
import com.example.catalog.model.Track;
import com.example.catalog.services.DataSourceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static com.example.catalog.utils.SpotifyUtils.isValidId;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    private final DataSourceService dataSourceService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public AlbumController(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }


    @GetMapping
    public ResponseEntity<List<Album>> getAllAlbums() {
        try {
            List<Album> albums = dataSourceService.getAllAlbums();
            if (albums == null || albums.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(albums);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObjectNode> getAlbumById(@PathVariable String id) {
        try {
            if (!isValidId(id)) {
                ObjectNode errorResponse = objectMapper.createObjectNode();
                errorResponse.put("message", "Invalid ID format");
                errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            Album album = dataSourceService.getAlbumById(id);
            if (album == null) {
                ObjectNode errorResponse = objectMapper.createObjectNode();
                errorResponse.put("message", "Album not found");
                errorResponse.put("status", HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }

            ObjectNode successResponse = objectMapper.valueToTree(album);
            return ResponseEntity.ok(successResponse);
        } catch (IOException e) {
            ObjectNode errorResponse = objectMapper.createObjectNode();
            errorResponse.put("message", "Internal server error");
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping
    public ResponseEntity<ObjectNode> addAlbum(@RequestBody Album album) {
        try {
            if (album == null || album.getId() == null || !isValidId(album.getId())) {
                ObjectNode errorResponse = objectMapper.createObjectNode();
                errorResponse.put("message", "Invalid input - album data or ID is malformed");
                errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }
            dataSourceService.addAlbum(album);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IOException e) {
            ObjectNode errorResponse = objectMapper.createObjectNode();
            errorResponse.put("message", "Internal server error");
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ObjectNode> updateAlbum(@PathVariable String id, @RequestBody Album updatedAlbum) {
        try {
            if (!isValidId(id)) {
                ObjectNode errorResponse = objectMapper.createObjectNode();
                errorResponse.put("message", "Invalid ID format");
                errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            Album existingAlbum = dataSourceService.getAlbumById(id);
            if (existingAlbum == null) {
                ObjectNode errorResponse = objectMapper.createObjectNode();
                errorResponse.put("message", "Album not found");
                errorResponse.put("status", HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
            dataSourceService.updateAlbum(id, updatedAlbum);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (IOException e) {
            ObjectNode errorResponse = objectMapper.createObjectNode();
            errorResponse.put("message", "Internal server error");
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ObjectNode> deleteAlbum(@PathVariable String id) {
        try {
            if (!isValidId(id)) {
                ObjectNode errorResponse = objectMapper.createObjectNode();
                errorResponse.put("message", "Invalid ID format");
                errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            Album existingAlbum = dataSourceService.getAlbumById(id);
            if (existingAlbum == null) {
                ObjectNode errorResponse = objectMapper.createObjectNode();
                errorResponse.put("message", "Album not found");
                errorResponse.put("status", HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
            dataSourceService.deleteAlbum(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IOException e) {
            ObjectNode errorResponse = objectMapper.createObjectNode();
            errorResponse.put("message", "Internal server error");
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/tracks")
    public ResponseEntity<ObjectNode> getTracksByAlbumId(@PathVariable String id) {
        try {
            if (!isValidId(id)) {
                ObjectNode errorResponse = objectMapper.createObjectNode();
                errorResponse.put("message", "Invalid ID format");
                errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            List<Track> tracks = dataSourceService.getTracksByAlbumId(id);
            if (tracks == null || tracks.isEmpty()) {
                ObjectNode errorResponse = objectMapper.createObjectNode();
                errorResponse.put("message", "Tracks not found");
                errorResponse.put("status", HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }

            ObjectNode successResponse = objectMapper.createObjectNode();
            successResponse.set("tracks", objectMapper.valueToTree(tracks));
            return ResponseEntity.ok(successResponse);
        } catch (IOException e) {
            ObjectNode errorResponse = objectMapper.createObjectNode();
            errorResponse.put("message", "Internal server error");
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/{id}/tracks")
    public ResponseEntity<ObjectNode> addTrackToAlbum(@PathVariable String id, @RequestBody Track track) {
        try {
            if (!isValidId(id) || track == null || track.getId() == null || !isValidId(track.getId())) {
                ObjectNode errorResponse = objectMapper.createObjectNode();
                errorResponse.put("message", "Invalid input - track or album ID is malformed");
                errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }
            dataSourceService.addTrackToAlbum(id, track);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IOException e) {
            ObjectNode errorResponse = objectMapper.createObjectNode();
            errorResponse.put("message", "Internal server error");
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}/tracks/{trackId}")
    public ResponseEntity<ObjectNode> updateTrackInAlbum(@PathVariable String id, @PathVariable String trackId, @RequestBody Track updatedTrack) {
        try {
            if (!isValidId(id) || !isValidId(trackId)) {
                ObjectNode errorResponse = objectMapper.createObjectNode();
                errorResponse.put("message", "Invalid ID format");
                errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            Album album = dataSourceService.getAlbumById(id);
            if (album == null) {
                ObjectNode errorResponse = objectMapper.createObjectNode();
                errorResponse.put("message", "Album not found");
                errorResponse.put("status", HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
            dataSourceService.updateTrackInAlbum(id, trackId, updatedTrack);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (IOException e) {
            ObjectNode errorResponse = objectMapper.createObjectNode();
            errorResponse.put("message", "Internal server error");
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}/tracks/{trackId}")
    public ResponseEntity<ObjectNode> deleteTrackFromAlbum(@PathVariable String id, @PathVariable String trackId) {
        try {
            if (!isValidId(id) || !isValidId(trackId)) {
                ObjectNode errorResponse = objectMapper.createObjectNode();
                errorResponse.put("message", "Invalid ID format");
                errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            Album album = dataSourceService.getAlbumById(id);
            if (album == null) {
                ObjectNode errorResponse = objectMapper.createObjectNode();
                errorResponse.put("message", "Album not found");
                errorResponse.put("status", HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
            dataSourceService.deleteTrackFromAlbum(id, trackId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IOException e) {
            ObjectNode errorResponse = objectMapper.createObjectNode();
            errorResponse.put("message", "Internal server error");
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}