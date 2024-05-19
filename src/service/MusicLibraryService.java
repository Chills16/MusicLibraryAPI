package service;

import dataAccessObject.SongDAO;
import model.Playlist;
import model.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.util.Collections;
import java.util.stream.Collectors;

public class MusicLibraryService {
    private final SongDAO songDao;
    private final List<Playlist> playlists;

    public MusicLibraryService(SongDAO songDao) {
        this.songDao = songDao;
        this.playlists = new ArrayList<>();
    }

    // Song Operations
    public void addSong(Song song) {
        songDao.addSong(song);
    }

    public boolean removeSongByTitle(String title) {
        Song song = songDao.findSongByTitle(title);
        return song != null && songDao.removeSong(song);
    }

    public boolean updateSong(String oldTitle, String newTitle, String newArtist, String newGenre) {
        Song oldSong = songDao.findSongByTitle(oldTitle);
        if (oldSong != null) {
            boolean removed = songDao.removeSong(oldSong);
            if (removed) {
                songDao.addSong(new Song(newTitle, newArtist, newGenre));
                return true;
            }
        }
        return false;
    }

    public List<Song> getAllSongs() {
        return songDao.getAllSongs();
    }

    public Song findSongByTitle(String title) {
        return songDao.findSongByTitle(title);
    }

    // Playlist Operations
    public void createPlaylist(String name) {
        Playlist newPlaylist = new Playlist(name);
        playlists.add(newPlaylist);
    }

    public void addSongToPlaylist(String playlistName, Song song) {
        Optional<Playlist> playlist = playlists.stream()
                .filter(p -> p.getName().equalsIgnoreCase(playlistName))
                .findFirst();

        playlist.ifPresent(p -> p.addSong(song));
    }

    public boolean removeSongFromPlaylist(String playlistName, Song song) {
        Optional<Playlist> playlist = playlists.stream()
                .filter(p -> p.getName().equalsIgnoreCase(playlistName))
                .findFirst();

        if (playlist.isPresent()) {
            return playlist.get().removeSong(song);
        }
        return false;
    }

    public List<Song> getSongsInPlaylist(String playlistName) {
        Optional<Playlist> playlist = playlists.stream()
                .filter(p -> p.getName().equalsIgnoreCase(playlistName))
                .findFirst();

        if (playlist.isPresent()) {
            return playlist.get().getSongs();
        }
        return new ArrayList<>();
    }

    public boolean deletePlaylist(String name) {
        return playlists.removeIf(playlist -> playlist.getName().equalsIgnoreCase(name));
    }

    public List<Song> searchSongs(String query) {
        List<Song> allSongs = songDao.getAllSongs(); // Fixed reference to use songDao
        return allSongs.stream()
                .filter(song -> song.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        song.getArtist().toLowerCase().contains(query.toLowerCase()) ||
                        song.getGenre().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Song> getShuffledSongs() {
        List<Song> shuffledSongs = new ArrayList<>(songDao.getAllSongs()); // Fixed reference to use songDao
        Collections.shuffle(shuffledSongs);
        return shuffledSongs;
    }

    public List<Playlist> searchPlaylists(String query) {
        return playlists.stream()
                .filter(playlist -> playlist.getName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
}


