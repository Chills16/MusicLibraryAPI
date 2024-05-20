package service;

import dataAccessObject.SongDAO;
import model.Playlist;
import model.Song;
import spotify.SpotifyAuth;
import spotify.SpotifySearch;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class manages the music library, including adding songs, creating playlists,
 * and interacting with the Spotify API to fetch songs.
 */
public class MusicLibraryService {
    private final SongDAO songDao; // Data access object to interact with song data.
    private final List<Playlist> playlists; // List to hold all playlists.

    /**
     * Constructor for MusicLibraryService.
     * @param songDao Data access object for managing song persistence.
     */
    public MusicLibraryService(SongDAO songDao) {
        this.songDao = songDao;
        this.playlists = new ArrayList<>(); // Initializes the playlist collection.
    }

    /**
     * Adds a song to the music library after checking its existence and fetching from Spotify.
     * @param title The title of the song.
     * @param artist The artist of the song.
     * @param optionalGenre The genre of the song (optional).
     * @param scanner A scanner object for reading user input.
     */
    public void addSong(String title, String artist, String optionalGenre, Scanner scanner) {
        try {
            if (songDao.findSongByTitle(title) != null) { // Check if the song already exists in the database.
                System.out.println("This song is already added.");
                return;
            }

            String accessToken = SpotifyAuth.getAccessToken(); // Fetches a valid Spotify access token.
            Map<String, String> searchResults = SpotifySearch.searchSong(accessToken, title + " " + artist); // Searches for the song on Spotify.

            if (!searchResults.isEmpty()) { // Check if any results were found.
                List<String> keysAsArray = new ArrayList<>(searchResults.keySet());
                for (int i = 0; i < keysAsArray.size(); i++) {
                    System.out.println((i + 1) + ": " + keysAsArray.get(i)); // Display each song found.
                }

                System.out.println("Select the desired song option:");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consumes the line after the input.

                String selectedKey = keysAsArray.get(choice - 1); // Selects the chosen song.
                String selectedUrl = searchResults.get(selectedKey);

                String[] details = selectedKey.split(" - "); // Splits the key into artist and song title.
                String selectedName = details[1];
                String selectedArtist = details[0];

                Song song = new Song(selectedName, selectedArtist, optionalGenre.isEmpty() ? "Unknown Genre" : optionalGenre, selectedUrl);
                songDao.addSong(song); // Adds the new song to the database.
                System.out.println("Song added: " + song);
            } else {
                System.out.println("No results found on Spotify.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error fetching from Spotify.");
        }
    }

    /**
     * Retrieves all songs from the music library.
     * @return A list of all songs.
     */
    public List<Song> getAllSongs() {
        return songDao.getAllSongs();
    }

    /**
     * Removes a song from the library and all playlists where it may appear.
     * @param title The title of the song to be removed.
     * @return true if the song was successfully removed, false otherwise.
     */
    public boolean removeSong(String title) {
        Song song = songDao.findSongByTitle(title);
        if (song != null && songDao.removeSong(song)) { // If the song is found and successfully removed.
            playlists.forEach(playlist -> playlist.removeSong(song)); // Remove the song from all playlists.
            return true;
        }
        return false;
    }

    /**
     * Creates a new playlist with a given name.
     * @param name The name of the new playlist.
     */
    public void createPlaylist(String name) {
        playlists.add(new Playlist(name)); // Adds a new playlist to the list.
    }

    /**
     * Finds a song by its standardized name format "Artist - Title".
     * @param standardizedName The standardized name of the song.
     * @return The song if found, null otherwise.
     */
    public Song findSongByStandardizedName(String standardizedName) {
        return songDao.getAllSongs().stream()
                .filter(song -> song.getStandardizedName().equalsIgnoreCase(standardizedName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Adds a song to a specified playlist by its standardized name.
     * @param playlistName The name of the playlist.
     * @param songStandardizedName The standardized name of the song.
     * @return true if the song was added to the playlist, false otherwise.
     */
    public boolean addSongToPlaylist(String playlistName, String songStandardizedName) {
        Optional<Playlist> playlist = playlists.stream()
                .filter(p -> p.getName().equalsIgnoreCase(playlistName))
                .findFirst();
        Song song = findSongByStandardizedName(songStandardizedName);

        if (song != null && playlist.isPresent()) {
            playlist.get().addSong(song);
            return true;
        }
        return false;
    }

    /**
     * Removes a song from a specified playlist by its standardized name.
     * @param playlistName The name of the playlist.
     * @param songStandardizedName The standardized name of the song.
     * @return true if the song was removed from the playlist, false otherwise.
     */
    public boolean removeSongFromPlaylist(String playlistName, String songStandardizedName) {
        Optional<Playlist> playlist = playlists.stream()
                .filter(p -> p.getName().equalsIgnoreCase(playlistName))
                .findFirst();
        Song song = findSongByStandardizedName(songStandardizedName);

        if (song != null && playlist.isPresent()) {
            return playlist.get().removeSong(song);
        }
        return false;
    }

    /**
     * Retrieves all songs in a specified playlist.
     * @param playlistName The name of the playlist.
     * @return A list of songs in the specified playlist.
     */
    public List<Song> viewSongsInPlaylist(String playlistName) {
        Optional<Playlist> playlist = playlists.stream()
                .filter(p -> p.getName().equalsIgnoreCase(playlistName))
                .findFirst();

        if (playlist.isPresent()) {
            return playlist.get().getSongs();
        }
        return new ArrayList<>();
    }

    /**
     * Deletes a playlist by name.
     * @param name The name of the playlist to be deleted.
     * @return true if the playlist was successfully deleted, false otherwise.
     */
    public boolean deletePlaylist(String name) {
        return playlists.removeIf(playlist -> playlist.getName().equalsIgnoreCase(name));
    }

    /**
     * Searches for songs in the library that match a given query.
     * @param query The search query.
     * @return A list of songs that match the query.
     */
    public List<Song> searchSongs(String query) {
        return songDao.getAllSongs().stream()
                .filter(song -> song.getTitle().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Searches for playlists that match a given query.
     * @param query The search query.
     * @return A list of playlists that match the query.
     */
    public List<Playlist> searchPlaylists(String query) {
        return playlists.stream()
                .filter(playlist -> playlist.getName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
}



