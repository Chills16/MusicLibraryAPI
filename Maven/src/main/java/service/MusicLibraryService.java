package service;

import dataAccessObject.SongDAO;
import model.Playlist;
import model.Song;
import spotify.SpotifyAuth;
import spotify.SpotifySearch;
import java.util.*;
import java.util.stream.Collectors;

public class MusicLibraryService {
    private final SongDAO songDao;
    private final List<Playlist> playlists;

    public MusicLibraryService(SongDAO songDao) {
        this.songDao = songDao;
        this.playlists = new ArrayList<>();
    }

    public void addSong(String title, String artist, String optionalGenre, Scanner scanner) {
        try {
            // First, check if the song already exists in the library
            if (songDao.findSongByTitle(title) != null) {
                System.out.println("This song is already added.");
                return; // Exit if the song is already present
            }

            // Fetch available songs from Spotify based on the provided title and artist
            String accessToken = SpotifyAuth.getAccessToken(); // Assume this gets a valid token
            Map<String, String> searchResults = SpotifySearch.searchSong(accessToken, title + " " + artist);

            if (!searchResults.isEmpty()) {
                List<String> keysAsArray = new ArrayList<>(searchResults.keySet());
                for (int i = 0; i < keysAsArray.size(); i++) {
                    System.out.println((i + 1) + ": " + keysAsArray.get(i));
                }

                System.out.println("Select the desired song option:");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline

                String selectedKey = keysAsArray.get(choice - 1);
                String selectedUrl = searchResults.get(selectedKey);

                String[] details = selectedKey.split(" - "); // Assuming " - " is used as the delimiter
                String selectedName = details[1]; // Adjust index based on your split results
                String selectedArtist = details[0];

                // Construct the song object and add it to the database
                Song song = new Song(selectedName, selectedArtist, optionalGenre.isEmpty() ? "Unknown Genre" : optionalGenre, selectedUrl);
                songDao.addSong(song);
                System.out.println("Song added: " + song);
            } else {
                System.out.println("No results found on Spotify.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error fetching from Spotify.");
        }
    }

    public List<Song> getAllSongs() {
        return songDao.getAllSongs();
    }

    public boolean removeSong(String title) {
        Song song = songDao.findSongByTitle(title);
        if (song != null) {
            boolean removed = songDao.removeSong(song);
            if (removed) {
                // Optionally remove from playlists as well
                playlists.forEach(playlist -> playlist.removeSong(song));
                return true;
            }
        }
        return false;
    }

    public void createPlaylist(String name) {
        Playlist newPlaylist = new Playlist(name);
        playlists.add(newPlaylist);
    }

    public Song findSongByStandardizedName(String standardizedName) {
        return songDao.getAllSongs().stream()
                .filter(song -> song.getStandardizedName().equalsIgnoreCase(standardizedName))
                .findFirst()
                .orElse(null);
    }

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

    public List<Song> viewSongsInPlaylist(String playlistName) {
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
        return songDao.getAllSongs().stream()
                .filter(song -> song.getTitle().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Playlist> searchPlaylists(String query) {
        return playlists.stream()
                .filter(playlist -> playlist.getName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
}


