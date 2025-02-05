package com.example.catalog.services;

import com.example.catalog.model.Album;
import com.example.catalog.model.Song;
import com.example.catalog.model.Track;
import com.example.catalog.model.Artist;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;


import java.io.IOException;

public interface  DataSourceService {

    Artist getArtistById(String id) throws IOException;
    List<Artist> getAllArtists() throws IOException;

    List<Artist> getArtists( ) throws IOException;

    boolean addArtist( Artist artist) throws IOException;
    boolean updateArtist(String id, Artist artist) throws IOException;
    boolean deleteArtist(String id) throws IOException;
    List<Album> getAllAlbums() throws IOException;

    List<Album> getAlbums( ) throws IOException;

    Album getAlbumById( String id) throws IOException;
    boolean addAlbum(Album album) throws IOException;
    boolean updateAlbum(String id, Album updatedAlbum) throws IOException;
    boolean deleteAlbum(String id) throws IOException;
    List<Track> getTracksByAlbumId(String albumId) throws IOException;

    boolean deleteAlbumById( String albumId ) throws IOException;

    List<Track> getAlbumTracks( String albumId ) throws IOException;

    boolean addTrackToAlbum( String albumId, Track track) throws IOException;
    boolean updateTrackInAlbum(String albumId, String trackId, Track updatedTrack) throws IOException;

    boolean updateTrack( String albumId, String trackId, Track updatedTrack ) throws IOException;

    boolean deleteTrackFromAlbum( String albumId, String trackId) throws IOException;

    boolean deleteArtistById( String artistId ) throws IOException;

    List<Album> getArtistAlbums( String artistId ) throws IOException;

    List<Song> getArtistSongs( String artistId ) throws IOException;

    List<Song> getAllSongs() throws IOException;
    Song getSongById(String id) throws IOException;
    boolean addSong(Song song) throws IOException;
    boolean updateSong(String id, Song updatedSong) throws IOException;
    boolean deleteSong(String id) throws IOException;
    List<Album> getAlbumsByArtist(String artistId) throws IOException;
    List<Song> getSongsByArtist(String artistId) throws IOException;

    boolean deleteSongById( String songId ) throws IOException;
}