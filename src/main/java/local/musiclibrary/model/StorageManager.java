package local.musiclibrary.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import local.musiclibrary.dao.SongDAO;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the persistent storage of playlists and their associated songs in a music library application.
 * It uses JSON to serialize playlists to and deserialize playlists from a file.
 * This class also ensures that songs within the playlists are refreshed with up-to-date instances from a SongDAO,
 * which helps maintain consistency and integrity of the data across application sessions.
 */
@SuppressWarnings("all")
public class StorageManager {
    private static final String FILE_PATH = "music_library.json";  // Path to the JSON file where playlists are stored.
    private static final Gson gson = new Gson();  // Gson instance for JSON processing.

    /**
     * Saves the current state of playlists to a JSON file.
     * @param playlists A list of playlists to save.
     */
    public static void saveData(List<Playlist> playlists) {
        String json = gson.toJson(playlists);  // Convert playlists to a JSON string.
        try {
            Files.write(Paths.get(FILE_PATH), json.getBytes());  // Write the JSON string to a file.
        } catch (IOException e) {
            e.printStackTrace();  // Log any IO exceptions that occur during file writing.
        }
    }

    /**
     * Loads playlists from a JSON file and refreshes their song data using a provided SongDAO.
     * This method ensures that songs in playlists are aligned with the latest versions managed by the SongDAO.
     * @param songDao A DAO for accessing and updating song data.
     * @return A list of refreshed playlists.
     */
    public static List<Playlist> loadData(SongDAO songDao) {
        try {
            String json = new String(Files.readAllBytes(Paths.get(FILE_PATH)));  // Read the JSON string from a file.
            Type listType = new TypeToken<List<Playlist>>(){}.getType();  // Define the target type for deserialization.
            List<Playlist> loadedPlaylists = gson.fromJson(json, listType);  // Convert the JSON string back to a List of Playlist objects.

            for (Playlist playlist : loadedPlaylists) {  // Iterate over each loaded playlist.
                List<Song> refreshedSongs = new ArrayList<>();  // Prepare a new list to store refreshed songs.
                for (Song song : playlist.getSongs()) {  // Iterate over each song in the playlist.
                    Song refreshedSong = songDao.findSongByTitle(song.getTitle());  // Attempt to refresh the song from DAO.
                    if (refreshedSong != null) {
                        refreshedSongs.add(refreshedSong);  // If found, add the refreshed song to the list.
                    } else {
                        songDao.addSong(song);  // If not found, optionally add the song to DAO and then to the list.
                        refreshedSongs.add(song);
                    }
                }
                playlist.setSongs(refreshedSongs);  // Update the playlist's songs with the refreshed list.
            }
            return loadedPlaylists;  // Return the refreshed list of playlists.
        } catch (IOException e) {
            e.printStackTrace();  // Log any IO exceptions that occur during file reading.
            return new ArrayList<>();  // Return an empty list if an error occurs.
        }
    }
}

