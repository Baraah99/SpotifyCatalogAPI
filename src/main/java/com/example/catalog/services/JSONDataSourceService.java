package com.example.catalog.services;
import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.example.catalog.model.Song;
import com.example.catalog.model.Track;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Service
public class JSONDataSourceService implements DataSourceService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private JsonNode loadJsonData(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);

        return objectMapper.readTree(resource.getFile());
    }

    private void saveJsonData(JsonNode data,String path) throws IOException {
        File file = new File(path);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
    }


    @Override
    public List<Artist> getAllArtists() throws IOException {
        JsonNode artistsNode = loadJsonData("data-test/popular_artists.json");
        List<Artist> artistList = new ArrayList<>();
        Iterator<String> fieldNames = artistsNode.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode artistNode = artistsNode.get(fieldName);
            Artist artist = objectMapper.treeToValue(artistNode, Artist.class);
            artistList.add(artist);
        }

        return artistList;
    }

    @Override
    public List< Artist > getArtists( ) throws IOException {
        return List.of( );
    }


    @Override
    public Artist getArtistById(String id) throws IOException {
        JsonNode artists = loadJsonData("data-test/popular_artists.json");
        JsonNode artistNode = artists.get(id);
        if (artistNode == null) {
            return null;
        }
        return objectMapper.treeToValue(artistNode, Artist.class);
    }


    @Override
    public boolean addArtist(Artist artist) {
        try {
            JsonNode artistsNode = loadJsonData("data-test/popular_artists.json");
            if (!(artistsNode instanceof ObjectNode)) {
                return false;
            }
            ((ObjectNode) artistsNode).set(artist.getId(), objectMapper.valueToTree(artist));
            objectMapper.writeValue(new ClassPathResource("data-test/popular_artists.json").getFile(), artistsNode);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateArtist(String id, Artist artist) {
        try {
            JsonNode artistsNode = loadJsonData("data-test/popular_artists.json");
            if (artistsNode.has(id)) {
                ((ObjectNode) artistsNode).set(id, objectMapper.valueToTree(artist));
                objectMapper.writeValue(new ClassPathResource("data-test/popular_artists.json").getFile(), artistsNode);
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean deleteArtist(String id) {
        try {
            JsonNode artistsNode = loadJsonData("data-test/popular_artists.json");

            if (artistsNode.has(id)) {
                ((ObjectNode) artistsNode).remove(id);
                objectMapper.writeValue(new ClassPathResource("data-test/popular_artists.json").getFile(), artistsNode);
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public List<Album> getAllAlbums() throws IOException {
        JsonNode albumsNode = loadJsonData("data-test/albums.json");
        List<Album> albumList = new ArrayList<>();
        Iterator<String> fieldNames = albumsNode.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode albumNode = albumsNode.get(fieldName);
            Album album = objectMapper.treeToValue(albumNode, Album.class);
            albumList.add(album);
        }

        return albumList;
    }

    @Override
    public List< Album > getAlbums( ) throws IOException {
        return List.of( );
    }

    @Override
    public Album getAlbumById(String id) throws IOException {

        JsonNode albums = loadJsonData("data-test/albums.json");
        JsonNode albumNode = albums.get(id);
        if (albumNode == null) {
            return null;
        }
        return objectMapper.treeToValue(albumNode, Album.class);

    }

    @Override
    public boolean addAlbum(Album album) {
        try {
            JsonNode albumsNode = loadJsonData("data-test/albums.json");

            if (albumsNode.has(album.getId())) {
                return false;
            }

            ((ObjectNode) albumsNode).set(album.getId(), objectMapper.valueToTree(album));
            objectMapper.writeValue(new ClassPathResource("data-test/albums.json").getFile(), albumsNode);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteAlbum(String id) {
        try {
            JsonNode albumsNode = loadJsonData("data-test/albums.json");

            if (albumsNode.has(id)) {
                ((ObjectNode) albumsNode).remove(id);
                objectMapper.writeValue(new ClassPathResource("data-test/albums.json").getFile(), albumsNode);
                return true;
            }

            return false;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateAlbum(String id, Album updatedAlbum) {
        try {
            JsonNode albumsNode = loadJsonData("data-test/albums.json");


            if (albumsNode.has(id)) {
                ((ObjectNode) albumsNode).set(id, objectMapper.valueToTree(updatedAlbum));
                objectMapper.writeValue(new ClassPathResource("data-test/albums.json").getFile(), albumsNode);
                return true;
            }

            return false;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Track> getTracksByAlbumId(String albumId) throws IOException {
        Album album = getAlbumById(albumId);
        return (album != null) ? album.getTracks() : Collections.emptyList();
    }

    @Override
    public boolean deleteAlbumById( String albumId ) throws IOException {
        return false;
    }

    @Override
    public List< Track > getAlbumTracks( String albumId ) throws IOException {
        return List.of( );
    }

    @Override
    public boolean addTrackToAlbum(String albumId, Track track) {
        try {
            Album album = getAlbumById(albumId);

            if (album != null) {
                album.getTracks().add(track);
                updateAlbum(albumId, album);
                return true;
            }

            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateTrackInAlbum(String albumId, String trackId, Track updatedTrack) {
        try {
            Album album = getAlbumById(albumId);

            if (album != null) {
                List<Track> tracks = album.getTracks();
                for (int i = 0; i < tracks.size(); i++) {
                    if (tracks.get(i).getId().equals(trackId)) {
                        tracks.set(i, updatedTrack);
                        updateAlbum(albumId, album);
                        return true;
                    }
                }
            }

            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateTrack( String albumId, String trackId, Track updatedTrack ) throws IOException {
        return false;
    }

    @Override
    public boolean deleteTrackFromAlbum(String albumId, String trackId) {
        try {
            Album album = getAlbumById(albumId);
            if (album != null) {
                boolean trackRemoved = album.getTracks().removeIf(track -> track.getId().equals(trackId));

                if (trackRemoved) {
                    updateAlbum(albumId, album);
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteArtistById( String artistId ) throws IOException {
        return false;
    }

    @Override
    public List< Album > getArtistAlbums( String artistId ) throws IOException {
        return List.of( );
    }

    @Override
    public List< Song > getArtistSongs( String artistId ) throws IOException {
        return List.of( );
    }

    @Override
    public List<Song> getAllSongs() throws IOException {
        JsonNode songs = loadJsonData("data-test/popular_songs.json");

        List<Song> songList = new ArrayList<>();
        songs.elements().forEachRemaining(songNode -> {
            try {
                songList.add(objectMapper.treeToValue(songNode, Song.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return songList;
    }

    @Override
    public Song getSongById(String id) throws IOException {
        JsonNode songsNode = loadJsonData("data-test/popular_songs.json");

        JsonNode songNode = null;
        Iterator<JsonNode> tracks = songsNode.iterator();
        while (tracks.hasNext()) {
            JsonNode track = tracks.next();
            if (track.has("id") && track.get("id").asText().equals(id)) {
                songNode =track;
                break;
            }
        }

        if (songNode == null) {
            return null;
        }

        return objectMapper.treeToValue(songNode, Song.class);

    }

    @Override
    public boolean addSong(Song song) {
        try {
            JsonNode songsNode = loadJsonData("data-test/popular_songs.json");

            if (!songsNode.isArray()) {
                songsNode = objectMapper.createArrayNode();
            }

            ((ArrayNode) songsNode).add(objectMapper.valueToTree(song));
            objectMapper.writeValue(new ClassPathResource("data-test/popular_songs.json").getFile(), songsNode);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateSong(String id, Song updatedSong) throws IOException {

        JsonNode songsNode = loadJsonData("data-test/popular_songs.json");

        JsonNode songNode = null;
        Iterator<JsonNode> tracks = songsNode.iterator();

        while (tracks.hasNext()) {
            JsonNode track = tracks.next();
            if (track.has("id") && track.get("id").asText().equals(id)) {
                songNode = track;
                break;
            }
        }


        if (songNode == null) {
            return false;
        }

        ArrayNode updatedSongsNode = objectMapper.createArrayNode();
        for (JsonNode track : songsNode) {
            if (!track.equals(songNode)) {
                updatedSongsNode.add(track);
            }
        }
        objectMapper.writeValue(new ClassPathResource("data-test/popular_songs.json").getFile(), updatedSongsNode);

        return true;
    }

    @Override
    public boolean deleteSong(String id) throws IOException {
        JsonNode songsNode = loadJsonData("data-test/popular_songs.json");

        JsonNode songNode = null;
        Iterator<JsonNode> tracks = songsNode.iterator();

        while (tracks.hasNext()) {
            JsonNode track = tracks.next();
            if (track.has("id") && track.get("id").asText().equals(id)) {
                songNode = track;
                break;
            }
        }
        if (songNode == null) {
            return false;
        }

        ArrayNode updatedSongsNode = objectMapper.createArrayNode();

        for (JsonNode track : songsNode) {
            if (!track.equals(songNode)) {
                updatedSongsNode.add(track);
            }
        }

        objectMapper.writeValue(new ClassPathResource("data-test/popular_songs.json").getFile(), updatedSongsNode);
        return true;
    }

    @Override
    public List<Album> getAlbumsByArtist(String artistId) throws IOException {
        JsonNode songsNode = loadJsonData("data-test/popular_songs.json");
        List<Album> albums = new ArrayList<>();

        // Iterate through every song
        for (JsonNode song : songsNode) {
            // Check each artist inside the song
            for (JsonNode artist : song.get("artists")) {
                if (artist.has("id") && artist.get("id").asText().equals(artistId)) {
                    // Convert the album JsonNode to an Album object and add it to the list
                    Album album = objectMapper.treeToValue(song.get("album"), Album.class);
                    albums.add(album);
                    break;
                }
            }
        }
        return albums; // Return list of albums for the artist
    }

    @Override
    public List<Song> getSongsByArtist(String artistId) throws IOException {
        JsonNode songsNode = loadJsonData("data-test/popular_songs.json");
        List<Song> songs = new ArrayList<>();

        // Iterate through every song
        for (JsonNode song : songsNode) {
            // Check each artist inside the song
            for (JsonNode artist : song.get("artists")) {
                if (artist.has("id") && artist.get("id").asText().equals(artistId)) {
                    // Convert JsonNode to Song object and add it to the list
                    songs.add(objectMapper.treeToValue(song, Song.class));
                    break;
                }
            }
        }
        return songs; // Return list of songs for the artist
    }

    @Override
    public boolean deleteSongById( String songId ) throws IOException {
        return false;
    }

}