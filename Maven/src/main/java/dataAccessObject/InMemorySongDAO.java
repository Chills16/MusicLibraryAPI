package dataAccessObject;

import model.Song;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class InMemorySongDAO implements SongDAO {
    private final Map<String, Song> songMap;

    public InMemorySongDAO() {
        this.songMap = new HashMap<>();
    }

    @Override
    public void addSong(Song song) {
        // Normalize the key by converting the song title to lower case
        songMap.put(song.getTitle().toLowerCase(), song);
    }

    @Override
    public boolean removeSong(Song song) {
        // Remove the song by title, normalized to lower case
        return songMap.remove(song.getTitle().toLowerCase()) != null;
    }

    @Override
    public List<Song> getAllSongs() {
        // Return a new list containing all songs in the map
        return new ArrayList<>(songMap.values());
    }

    @Override
    public Song findSongByTitle(String title) {
        // Retrieve a song by title, normalized to lower case
        return songMap.get(title.toLowerCase());
    }
}



