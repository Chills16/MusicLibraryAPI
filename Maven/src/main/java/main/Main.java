package main;

import dataAccessObject.InMemorySongDAO;
import model.Playlist;
import model.Song;
import service.MusicLibraryService;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        InMemorySongDAO songDao = new InMemorySongDAO();
        MusicLibraryService service = new MusicLibraryService(songDao);

        while (true) {
            System.out.println("\nWelcome to the Music Library API");
            System.out.println("1. Add Song");
            System.out.println("2. List Songs");
            System.out.println("3. Remove Song");
            System.out.println("4. Create Playlist");
            System.out.println("5. Add Song to Playlist");
            System.out.println("6. Remove Song from Playlist");
            System.out.println("7. View Playlist");
            System.out.println("8. Delete Playlist");
            System.out.println("9. Search Songs");
            System.out.println("10. Search Playlists");
            System.out.println("11. Exit");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    String title = promptForInput("Enter song title: ");
                    String artist = promptForInput("Enter artist: ");
                    String genre = promptForInput("Enter genre (optional, press ENTER to skip): ");
                    service.addSong(title, artist, genre, scanner);
                    System.out.println("Song added successfully!");
                    break;

                case 2:
                    System.out.println("Songs in Library:");
                    for (Song song : service.getAllSongs()) {
                        System.out.println(song);
                    }
                    break;

                case 3:
                    String removeTitle = promptForInput("Enter song title to remove: ");
                    if (service.removeSong(removeTitle)) {
                        System.out.println("Song removed successfully!");
                    } else {
                        System.out.println("Song not found.");
                    }
                    break;

                case 4:
                    String createPlaylistName = promptForInput("Enter playlist name: ");
                    service.createPlaylist(createPlaylistName);
                    System.out.println("Playlist created successfully!");
                    break;

                case 5:
                    String addPlaylistName = promptForInput("Enter playlist name: ");
                    String songStandardizedName = promptForInput("Enter the song (Artist - Song): ");
                    if (service.addSongToPlaylist(addPlaylistName, songStandardizedName)) {
                        System.out.println("Song added to playlist successfully!");
                    } else {
                        System.out.println("Song not found or playlist does not exist.");
                    }
                    break;

                case 6:
                    String removePlaylistName = promptForInput("Enter playlist name: ");
                    String removeSongStandardizedName = promptForInput("Enter the song (Artist - Song) to remove: ");
                    if (service.removeSongFromPlaylist(removePlaylistName, removeSongStandardizedName)) {
                        System.out.println("Song removed from playlist successfully!");
                    } else {
                        System.out.println("Song not found in playlist or playlist does not exist.");
                    }
                    break;

                case 7:
                    String playlistToView = promptForInput("Enter playlist name to view: ");
                    System.out.println("Songs in " + playlistToView + " playlist:");
                    for (Song song : service.viewSongsInPlaylist(playlistToView)) {
                        System.out.println(song);
                    }
                    break;

                case 8:
                    String playlistToDelete = promptForInput("Enter playlist name to delete: ");
                    if (service.deletePlaylist(playlistToDelete)) {
                        System.out.println("Playlist deleted successfully!");
                    } else {
                        System.out.println("Playlist not found.");
                    }
                    break;

                case 9:
                    String searchQuery = promptForInput("Enter the song name to search for: ");
                    List<Song> foundSongs = service.searchSongs(searchQuery);
                    if (!foundSongs.isEmpty()) {
                        System.out.println("Found Songs:");
                        foundSongs.forEach(song -> System.out.println(song.getStandardizedName() + " - Listen on Spotify: " + song.getSpotifyUrl()));
                    } else {
                        System.out.println("No songs found matching your query.");
                    }
                    break;

                case 10:
                    String queryForPlaylists = promptForInput("Enter search query for playlists: ");
                    List<Playlist> foundPlaylists = service.searchPlaylists(queryForPlaylists);
                    System.out.println("Found Playlists:");
                    foundPlaylists.forEach(playlist -> System.out.println(playlist.getName()));
                    break;

                case 11:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static String promptForInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
