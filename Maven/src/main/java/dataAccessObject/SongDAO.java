package dataAccessObject;

import model.Song;
import java.util.List;

public interface SongDAO {
    void addSong(Song song);
    boolean removeSong(Song song);
    List<Song> getAllSongs();
    Song findSongByTitle(String title);
}

