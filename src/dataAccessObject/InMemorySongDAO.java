package dataAccessObject;

import model.Song;
import java.util.ArrayList;
import java.util.List;

public class InMemorySongDAO implements SongDAO {
    private List<Song> songList;

    public InMemorySongDAO() {
        this.songList = new ArrayList<>();
    }

    @Override
    public void addSong(Song song) {
        songList.add(song);
    }

    @Override
    public boolean removeSong(Song song) {
        return songList.remove(song);
    }

    @Override
    public List<Song> getAllSongs() {
        return new ArrayList<>(songList);
    }

    @Override
    public Song findSongByTitle(String title) {
        return songList.stream()
                .filter(song -> song.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }
}

