package com.example.catalog;

import static org.junit.jupiter.api.Assertions.*;

import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.example.catalog.model.Song;
import com.example.catalog.services.JSONDataSourceService;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;

public class SongControllerTest {


    @Test
    public void testUpdateNonExistentSong() throws IOException {
        JSONDataSourceService jsonDataSourceService = new JSONDataSourceService();

        Song updatedSong = new Song(
                "nonExistentSong", "Non Existent Song", 200000, 70, "spotify:track:nonExistentSong",
                new Album("album1", "Test Album", "spotify:album:album1", "2021-12-23", 10, null, null, null),
                Collections.singletonList(new Artist("artist1", "Test Artist", "spotify:artist:artist1"))
        );
        boolean result = jsonDataSourceService.updateSong("nonExistentSong", updatedSong);

        assertFalse(result);
    }

    @Test
    public void testDeleteNonExistentSong() throws IOException {
        JSONDataSourceService jsonDataSourceService = new JSONDataSourceService();

        boolean result = jsonDataSourceService.deleteSong("nonExistentSong");

        assertFalse(result);
    }
}