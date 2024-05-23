package local.musiclibrary.dao;

import local.musiclibrary.model.Song;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

// Implementation of SongDAO to manage songs in memory using a HashMap.
public class InMemorySongDAO implements local.musiclibrary.dao.SongDAO {
    // Map to store songs, using the song's title in lowercase as the key.
    private final Map<String, Song> songMap;

    // Constructor to initialize the HashMap when an instance of this class is created.
    public InMemorySongDAO() {
        this.songMap = new HashMap<>();
    }

    // Add a song to the map; if a song with the same title already exists, it will be overwritten.
    @Override
    public void addSong(Song song) {
        songMap.put(song.getTitle().toLowerCase(), song);
    }

    // Remove a song from the map by its title, return true if the song was found and removed.
    @Override
    public boolean removeSong(Song song) {
        return songMap.remove(song.getTitle().toLowerCase()) != null;
    }

    // Return a new list containing all the songs currently stored in the map.
    @Override
    public List<Song> getAllSongs() {
        return new ArrayList<>(songMap.values());
    }

    // Retrieve a song by its title; the title is converted to lowercase to ensure case-insensitivity.
    @Override
    public Song findSongByTitle(String title) {
        return songMap.get(title.toLowerCase());
    }
}




