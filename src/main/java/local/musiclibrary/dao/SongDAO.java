package local.musiclibrary.dao;

import local.musiclibrary.model.Song;
import java.util.List;

// Interface defining the basic operations for managing songs in a data repository.
public interface SongDAO {
    // Method to add a song to the data repository.
    void addSong(Song song);

    // Method to remove a song from the data repository.
    boolean removeSong(Song song);

    // Method to retrieve all songs from the data repository.
    List<Song> getAllSongs();

    // Method to find a song by its title in the data repository.
    Song findSongByTitle(String title);
}


