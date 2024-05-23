package local.musiclibrary.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Class representing a playlist which can contain multiple songs.
@SuppressWarnings("all")
public class Playlist {
    // The name of the playlist.
    private final String name;
    // List to store the songs in the playlist.
    private List<Song> songs;

    // Constructor to create a new playlist with a given name.
    public Playlist(String name) {
        this.name = name;
        this.songs = new ArrayList<>();
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    // Add a song to the playlist.
    public void addSong(Song song) {
        songs.add(song);
    }

    // Remove a song from the playlist.
    public boolean removeSong(Song songToRemove) {
        Iterator<Song> it = songs.iterator();
        while (it.hasNext()) {
            Song song = it.next();
            if (song.getTitle().equalsIgnoreCase(songToRemove.getTitle()) &&
                    song.getArtist().equalsIgnoreCase(songToRemove.getArtist())) {
                it.remove();
                return true;
            }
        }
        return false;
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



