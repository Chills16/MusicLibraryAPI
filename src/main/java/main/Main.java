package main;

// Import necessary classes from other packages to use them in this file.
import dataAccessObject.InMemorySongDAO;
import model.Playlist;
import model.Song;
import service.MusicLibraryService;

import java.util.List;
import java.util.Scanner;

// Define the Main class where the application starts.
public class Main {
    // Create a Scanner object to read input from the user via the console.
    private static final Scanner scanner = new Scanner(System.in);

    // Main method where the program execution begins.
    public static void main(String[] args) {
        // Create an instance of InMemorySongDAO to manage songs in memory.
        InMemorySongDAO songDao = new InMemorySongDAO();
        // Create an instance of MusicLibraryService to handle business logic related to songs and playlists.
        MusicLibraryService service = new MusicLibraryService(songDao);

        // Infinite loop to keep the program running until the user decides to exit.
        while (true) {
            // Print options available to the user in the console.
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

            // Read the user's choice as an integer.
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume the newline left by nextInt().

            // Switch statement to handle different choices based on user input.
            switch (option) {
                case 1:
                    // Prompt the user to enter details for a new song and add it to the library.
                    String title = promptForInput("Enter song title: ");
                    String artist = promptForInput("Enter artist: ");
                    String genre = promptForInput("Enter genre (optional, press ENTER to skip): ");
                    service.addSong(title, artist, genre, scanner);
                    System.out.println("Song added successfully!");
                    break;

                case 2:
                    // Display all songs currently in the library.
                    System.out.println("Songs in Library:");
                    for (Song song : service.getAllSongs()) {
                        System.out.println(song);
                    }
                    break;

                case 3:
                    // Prompt the user to enter a song title to remove it from the library.
                    String removeTitle = promptForInput("Enter song title to remove: ");
                    if (service.removeSong(removeTitle)) {
                        System.out.println("Song removed successfully!");
                    } else {
                        System.out.println("Song not found.");
                    }
                    break;

                case 4:
                    // Prompt the user to enter a name for a new playlist and create it.
                    String createPlaylistName = promptForInput("Enter playlist name: ");
                    service.createPlaylist(createPlaylistName);
                    System.out.println("Playlist created successfully!");
                    break;

                case 5:
                    // Prompt the user to add a song to a specified playlist.
                    String addPlaylistName = promptForInput("Enter playlist name: ");
                    String songStandardizedName = promptForInput("Enter the song (Artist - Song): ");
                    if (service.addSongToPlaylist(addPlaylistName, songStandardizedName)) {
                        System.out.println("Song added to playlist successfully!");
                    } else {
                        System.out.println("Song not found or playlist does not exist.");
                    }
                    break;

                case 6:
                    // Prompt the user to remove a song from a specified playlist.
                    String removePlaylistName = promptForInput("Enter playlist name: ");
                    String removeSongStandardizedName = promptForInput("Enter the song (Artist - Song) to remove: ");
                    if (service.removeSongFromPlaylist(removePlaylistName, removeSongStandardizedName)) {
                        System.out.println("Song removed from playlist successfully!");
                    } else {
                        System.out.println("Song not found in playlist or playlist does not exist.");
                    }
                    break;

                case 7:
                    // View all songs in a specified playlist.
                    String playlistToView = promptForInput("Enter playlist name to view: ");
                    System.out.println("Songs in " + playlistToView + " playlist:");
                    for (Song song : service.viewSongsInPlaylist(playlistToView)) {
                        System.out.println(song);
                    }
                    break;

                case 8:
                    // Prompt the user to delete a specified playlist.
                    String playlistToDelete = promptForInput("Enter playlist name to delete: ");
                    if (service.deletePlaylist(playlistToDelete)) {
                        System.out.println("Playlist deleted successfully!");
                    } else {
                        System.out.println("Playlist not found.");
                    }
                    break;

                case 9:
                    // Search for songs in the library that match a specified query.
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
                    // Search for playlists in the library that match a specified query.
                    String queryForPlaylists = promptForInput("Enter search query for playlists: ");
                    List<Playlist> foundPlaylists = service.searchPlaylists(queryForPlaylists);
                    System.out.println("Found Playlists:");
                    foundPlaylists.forEach(playlist -> System.out.println(playlist.getName()));
                    break;

                case 11:
                    // Exit the program.
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;

                default:
                    // Handle invalid options.
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Helper method to prompt the user for input and return the input as a string.
    private static String promptForInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}

