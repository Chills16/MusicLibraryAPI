package model;

import java.util.ArrayList;
import java.util.List;

// Class representing a playlist which can contain multiple songs.
public class Playlist {
    // The name of the playlist.
    private final String name;
    // List to store the songs in the playlist.
    private final List<Song> songs;

    // Constructor to create a new playlist with a given name.
    public Playlist(String name) {
        this.name = name;
        this.songs = new ArrayList<>();
    }

    // Add a song to the playlist.
    public void addSong(Song song) {
        songs.add(song);
    }

    // Remove a song from the playlist, return true if the song was found and removed.
    public boolean removeSong(Song song) {
        return songs.remove(song);
    }

    // Get all songs in the playlist.
    public List<Song> getSongs() {
        return songs;
    }

    // Get the name of the playlist.
    public String getName() {
        return name;
    }

    // Return a string representation of the playlist including its name and the songs it contains.
    @Override
    public String toString() {
        return "Playlist{" +
                "name='" + name + '\'' +
                ", songs=" + songs +
                '}';
    }
}



