package com.example.catalog.services;

import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.example.catalog.model.Song;
import com.example.catalog.model.Track;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class DatabaseDataSourceService implements DataSourceService {

    @Override
    public Artist getArtistById(String id) {
        //   return db.findById(id).orElse(null);
        return new Artist();
    }


    @Override
    public List<Artist> getAllArtists() throws IOException {
        return List.of();
    }

    @Override
    public List< Artist > getArtists( ) throws IOException {
        return List.of( );
    }


    @Override
    public boolean addArtist(Artist artist) throws IOException {

        return false;
    }

    @Override
    public boolean updateArtist(String id,Artist artist) throws IOException {

        return false;
    }

    @Override
    public boolean deleteArtist(String id) throws IOException {

        return false;
    }

    @Override
    public List<Album> getAllAlbums() throws IOException {
        return List.of();
    }

    @Override
    public List< Album > getAlbums( ) throws IOException {
        return List.of( );
    }

    @Override
    public Album getAlbumById(String id) throws IOException {
        return null;
    }

    @Override
    public boolean addAlbum(Album album) throws IOException {

        return false;
    }

    @Override
    public boolean updateAlbum(String id, Album updatedAlbum) throws IOException {

        return false;
    }

    @Override
    public boolean deleteAlbum(String id) throws IOException {

        return false;
    }

    @Override
    public List<Track> getTracksByAlbumId(String albumId) throws IOException {
        return List.of();
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
    public boolean addTrackToAlbum(String albumId, Track track) throws IOException {

        return false;
    }

    @Override
    public boolean updateTrackInAlbum(String albumId, String trackId, Track updatedTrack) throws IOException {
        return false;
    }

    @Override
    public boolean updateTrack( String albumId, String trackId, Track updatedTrack ) throws IOException {
        return false;
    }

    @Override
    public boolean deleteTrackFromAlbum(String albumId, String trackId) throws IOException {
        return false;
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
        return List.of();
    }

    @Override
    public Song getSongById(String id) throws IOException {
        return null;
    }

    @Override
    public boolean addSong(Song song) throws IOException {
        return false;
    }

    @Override
    public boolean updateSong(String id, Song updatedSong) throws IOException {
        return false;
    }

    @Override
    public boolean deleteSong(String id) throws IOException {
        return false;
    }

    @Override
    public List<Album> getAlbumsByArtist(String artistId) throws IOException {
        return List.of();
    }

    @Override
    public List<Song> getSongsByArtist(String artistId) throws IOException {
        return List.of();
    }

    @Override
    public boolean deleteSongById( String songId ) throws IOException {
        return false;
    }


}