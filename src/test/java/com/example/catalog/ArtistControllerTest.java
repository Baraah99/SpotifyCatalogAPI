package com.example.catalog;

import static org.junit.jupiter.api.Assertions.*;

import com.example.catalog.model.Artist;
import com.example.catalog.services.JSONDataSourceService;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ArtistControllerTest {

    @Test
    public void testGetAllArtists() throws IOException {
        JSONDataSourceService jsonDataSourceService = new JSONDataSourceService();

        // Call the method
        List<Artist> artists = jsonDataSourceService.getAllArtists();

        // Verify the result
        assertNotNull(artists);
        assertTrue(artists.size() > 0);
        assertEquals("The Weeknd", artists.get(0).getName()); // Example check, adjust for your data
    }

    @Test
    public void testAddArtist() throws IOException {
        JSONDataSourceService jsonDataSourceService = new JSONDataSourceService();

        Artist newArtist = new Artist("newId", "New Artist", 5000, List.of("pop"), 75, "spotify:newArtistUri");

        // Call the method
        boolean result = jsonDataSourceService.addArtist(newArtist);

        // Verify the result
        assertTrue(result);

        // Verify that the artist is now added
        Artist addedArtist = jsonDataSourceService.getArtistById("newId");
        assertNotNull(addedArtist);
        assertEquals("New Artist", addedArtist.getName());
    }

    @Test
    public void testUpdateArtist() throws IOException {
        JSONDataSourceService jsonDataSourceService = new JSONDataSourceService();

        // First, add the artist to update
        Artist newArtist = new Artist("updateId", "Artist To Update", 1000, List.of("rock"), 60, "spotify:artistUri");
        jsonDataSourceService.addArtist(newArtist);

        // Create an updated version of the artist
        Artist updatedArtist = new Artist("updateId", "Updated Artist", 5000, Arrays.asList("pop", "rock"), 85, "spotify:updatedUri");

        // Call the update method
        boolean result = jsonDataSourceService.updateArtist("updateId", updatedArtist);

        // Verify the result
        assertTrue(result);

        // Verify that the artist is updated
        Artist fetchedArtist = jsonDataSourceService.getArtistById("updateId");
        assertNotNull(fetchedArtist);
        assertEquals("Updated Artist", fetchedArtist.getName());
    }

    @Test
    public void testDeleteArtist() throws IOException {
        JSONDataSourceService jsonDataSourceService = new JSONDataSourceService();

        // First, add the artist to delete
        Artist newArtist = new Artist("deleteId", "Artist To Delete", 1000, List.of("pop"), 60, "spotify:deleteUri");
        jsonDataSourceService.addArtist(newArtist);

        // Call the delete method
        boolean result = jsonDataSourceService.deleteArtist("deleteId");

        // Verify the result
        assertTrue(result);

        // Verify that the artist is deleted
        Artist deletedArtist = jsonDataSourceService.getArtistById("deleteId");
        assertNull(deletedArtist);
    }
}