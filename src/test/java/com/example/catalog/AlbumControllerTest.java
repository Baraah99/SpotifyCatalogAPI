package com.example.catalog;

import static org.junit.jupiter.api.Assertions.*;

import com.example.catalog.model.Album;
import com.example.catalog.model.Track;
import com.example.catalog.services.JSONDataSourceService;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AlbumControllerTest {

    @Test
    public void testUpdateAlbum() throws IOException {
        JSONDataSourceService jsonDataSourceService = new JSONDataSourceService();

        // First, add the album to update
        Album newAlbum = new Album("updateId", "Album To Update", Arrays.asList(new Track("track1", "Track 1")));
        jsonDataSourceService.addAlbum(newAlbum);

        // Create an updated version of the album
        Album updatedAlbum = new Album("updateId", "Updated Album", Arrays.asList(new Track("track2", "Track 2")));

        // Call the update method
        boolean result = jsonDataSourceService.updateAlbum("updateId", updatedAlbum);

        // Verify the result
        assertTrue(result);

        // Verify that the album is updated
        Album fetchedAlbum = jsonDataSourceService.getAlbumById("updateId");
        assertNotNull(fetchedAlbum);
        assertEquals("Updated Album", fetchedAlbum.getName());
    }

    @Test
    public void testDeleteAlbum() throws IOException {
        JSONDataSourceService jsonDataSourceService = new JSONDataSourceService();

        // First, add the album to delete
        Album newAlbum = new Album("deleteId", "Album To Delete", Arrays.asList(new Track("track1", "Track 1")));
        jsonDataSourceService.addAlbum(newAlbum);

        // Call the delete method
        boolean result = jsonDataSourceService.deleteAlbum("deleteId");

        // Verify the result
        assertTrue(result);

        // Verify that the album is deleted
        Album deletedAlbum = jsonDataSourceService.getAlbumById("deleteId");
        assertNull(deletedAlbum);
    }

    @Test
    public void testAddAlbumThenUpdate() throws IOException {
        JSONDataSourceService jsonDataSourceService = new JSONDataSourceService();

        // Add the album to be updated
        Album newAlbum = new Album("updateAlbumId", "Album Before Update", Arrays.asList(new Track("track1", "Track 1")));
        jsonDataSourceService.addAlbum(newAlbum);

        // Now, update the album object we just added
        Album updatedAlbum = new Album("updateAlbumId", "Album After Update", Arrays.asList(new Track("track2", "Track 2")));

        // Call the update method
        boolean result = jsonDataSourceService.updateAlbum("updateAlbumId", updatedAlbum);

        // Verify the result
        assertTrue(result);

        // Verify the album was updated correctly
        Album fetchedAlbum = jsonDataSourceService.getAlbumById("updateAlbumId");
        assertNotNull(fetchedAlbum);
        assertEquals("Album After Update", fetchedAlbum.getName());
    }

    @Test
    public void testAddAlbumThenDelete() throws IOException {
        JSONDataSourceService jsonDataSourceService = new JSONDataSourceService();

        // Add the album to be deleted
        Album newAlbum = new Album("deleteAlbumId", "Album To Be Deleted", Arrays.asList(new Track("track1", "Track 1")));
        jsonDataSourceService.addAlbum(newAlbum);

        // Call the delete method
        boolean result = jsonDataSourceService.deleteAlbum("deleteAlbumId");

        // Verify the result
        assertTrue(result);

        // Verify that the album was deleted
        Album deletedAlbum = jsonDataSourceService.getAlbumById("deleteAlbumId");
        assertNull(deletedAlbum);
    }
}